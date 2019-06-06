package client.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.debernardi.archemii.R;

import java.io.StreamCorruptedException;
import java.util.ArrayList;

import client.activities.GameActivity;
import client.activities.JoinPartyActivity;
import client.activities.LobbyActivity;
import server.general.ServerGameHandler;
import shared.abilities.Ability;
import shared.entities.Entity;
import shared.entities.Player;
import shared.general.Level;
import shared.general.Party;
import shared.messages.ActionMessage;
import shared.messages.GameUpdateMessage;
import shared.messages.Message;
import shared.messages.PlayerInfoMessage;

import static android.content.Context.MODE_PRIVATE;

public class ClientGameHandler {

	public static ClientGameHandler handler;

	private Connection connection;
	private static Player player = new Player(0,0,0);

	private Context context;

	private SharedPreferences.OnSharedPreferenceChangeListener listener;

	private JoinPartyActivity joinPartyActivity;
	private LobbyActivity lobbyActivity;
	private GameActivity gameActivity;

	private Party party;
	private Level level;

	/**
	 * Start a new connection, listen for changes in the server-mode and start the game loop.
	 * @param context
	 * @author Bram Pulles
	 */
	private ClientGameHandler(Context context) {
		this.context = context;

		newConnection();
		listenForServermode();
		gameLoop();
	}

	public void gameLoop(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				int TICKSPEED = ServerGameHandler.TICKSPEED / 10;
				while (true) {
					long start = System.currentTimeMillis();

					if (gameActivity != null && level != null) {
						gameActivity.draw(level);
						player.invokeMove();
						if (player.getActions().size() > 0) {
							ActionMessage msg = new ActionMessage(player.getActions());
							sendMessage(msg);
							player.clearActions();
						}
					}

					long timeTook = System.currentTimeMillis() - start;
					try {
						if (timeTook > TICKSPEED) {
							timeTook = TICKSPEED;
						}
						Thread.sleep(TICKSPEED - timeTook);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	/**
	 * Start a new connection and start listening for messages.
	 * @author Bram Pulles
	 */
	private void newConnection(){
		if(connection != null){
			connection.stop();
		}

		connection = new Connection(isSingleplayer(), context);
		listenForMessages();
	}

	public static void init(Context context) {
		if (handler == null) {
			handler = new ClientGameHandler(context);
		}
	}

	/**
	 * Listen for messages from the server.
	 * @author Bram Pulles
	 * @author Steven Bronsveld
	 */
	public void listenForMessages() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// Wait until the connection is established.
				while(connection.isStarting()){
				}

				while (connection.isConnected()) {
					try {
						Message m = (Message) connection.getInputStream().readObject();
						handleInput(m);
					} catch (Exception e) {
						if ((e.getMessage() != null && e.getMessage().equals("Socket closed")) || e instanceof StreamCorruptedException) {
							Log.d("CONNECTION", "Connection to server was lost.");
							break;
						}
						e.printStackTrace();
					}
				}
				Log.d(Connection.TAG, "Stopped listening for messages.");
			}
		}).start();
	}


	/**
	 * Listen to the shared preferences if the servermode changes.
	 * If the servermode changes create a new connection.
	 * @author Bram Pulles
	 */
	private void listenForServermode(){
		SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.sharedpref_servermodeinfo), MODE_PRIVATE);

		// Set up a listener for connection information.
		listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
			public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
				Log.d("CONNECTION", "Changing connection to: " + (isSingleplayer() ? "singleplayer" : "multiplayer"));
				newConnection();
			}
		};

		prefs.registerOnSharedPreferenceChangeListener(listener);
	}

	/**
	 * Use the shared preferences to determine if the servermode is singleplayer.
	 * @return if the servermode is singleplayer.
	 * @author Bram Pulles
	 */
	private boolean isSingleplayer(){
		SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.sharedpref_servermodeinfo), MODE_PRIVATE);
		String currentmode = prefs.getString(context.getString(R.string.sharedpref_servermode), context.getString(R.string.online));
		return currentmode.equals(context.getString(R.string.offline));
	}

	/**
	 * Send a message to the server from the client.
	 * @param msg message
	 */
	public static void sendMessage(Message msg) {
		handler.connection.sendMessage(msg);
	}

	/**
	 * This method received the messages send by the server.
	 * @param m
	 * @author Steven Bronsveld
	 * @author Bram Pulles
	 */
	public void handleInput(Message m) {
//		Log.d(Connection.TAG, "Client receives message: " + m);

		switch (m.getType()){
			case "PartyJoinedMessage":
				partyJoinedMessage();
				break;
			case "Party":
				partyMessage((Party)m);
				break;
			case "PlayerInfoMessage":
				playerInfoMessage((PlayerInfoMessage) m);
				break;
			case "Level":
				startGame((Level) m);
				break;
			case "GameUpdateMessage":
				updateGame((GameUpdateMessage) m);
				break;
		}
	}

	private void updateGame(GameUpdateMessage m) {
		for (Entity e : m.getChanges()) {
			if (e.getUUID().equals(player.getUUID())) {
				transferTransientPlayer((Player) e);
			}
			level.updateEntity(e);
		}
	}

	/**
	 * Transfer certain transient properties from the old player to the new player
	 * @param p The new player
	 */
	private synchronized void transferTransientPlayer(Player p) {
		ArrayList<Ability> abilities = player.getAbilities();
		double direction = player.direction;

		player = p;
		player.setAbilities(abilities);
		player.setActions(new ArrayList<Ability>());
		player.direction = direction;
		gameActivity.updateHealth(player.getHealth());
	}

	/**
	 * Set the level and start the game activity.
	 * @param level
	 * @author Bram Pulles
	 * @author Steven Bronsveld
	 */
	private void startGame(final Level level) {
		this.level = level;
		Intent intGame = new Intent(context, GameActivity.class);
		context.startActivity(intGame);
	}

	/**
	 * Open the lobby activity.
	 * @author Bram Pulles
	 */
	private void partyJoinedMessage(){
		joinPartyActivity.openLobby();
	}

	/**
	 * Send the new info to the lobby activity.
	 * @param party update party.
	 * @author Steven Bronsveld
	 * @author Bram Pulles
	 */
	private void partyMessage(Party party){
		this.party = party;
		if(lobbyActivity != null)
			lobbyActivity.updateParty(party);
	}

	/**
	 * This function is called when the server and client established a connection
	 * and the player is created on the server. The player can send his custom
	 * name to the server. The settings screen can also call this function for a name update.
	 * @author Bram Pulles
	 */
	public void playerInfoMessage(PlayerInfoMessage m){
		player = m.getPlayer();
		player.setAbilities(m.getAbilities());
		player.setActions(new ArrayList<Ability>());
		sendPlayerInfoMessage();
	}

	/**
	 * Send a message to the server with the player information.
	 * @author Bram Pulles
	 * @author Steven Bronsveld
	 */
	public void sendPlayerInfoMessage() {
		SharedPreferences sharedPrefs = context.getSharedPreferences(context.getString(R.string.sharedpref_playerinfo), MODE_PRIVATE);
		String username = sharedPrefs.getString("username", "-");

		player.setName(username);
		sendMessage(new PlayerInfoMessage(player));
	}

	/**
	 * @param joinPartyActivity
	 * @author Bram Pulles
	 */
	public void setJoinPartyActivity(JoinPartyActivity joinPartyActivity){
		this.joinPartyActivity = joinPartyActivity;
	}

	/**
	 * @param lobbyActivity
	 * @author Bram Pulles
	 */
	public void setLobbyActivity(LobbyActivity lobbyActivity){
		this.lobbyActivity = lobbyActivity;
	}

	public void setGameActivity(GameActivity gameActivity) {
		this.gameActivity = gameActivity;
	}

	public Player getPlayer(){
		return player;
	}

	public Party getParty() {
		return party;
	}
}
