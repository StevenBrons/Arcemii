package client.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.debernardi.archemii.R;

import client.activities.JoinPartyActivity;
import client.activities.LobbyActivity;
import shared.entities.Player;
import shared.general.Party;
import shared.messages.Message;
import shared.messages.PlayerInfoMessage;

import static android.content.Context.MODE_PRIVATE;

public class ClientGameHandler {

	public static ClientGameHandler handler;
	private Connection connection;
	private Player player = new Player(36,48,0);

	private Context context;

	private SharedPreferences.OnSharedPreferenceChangeListener listener;
	private Boolean listeningForMessages;

	private JoinPartyActivity joinPartyActivity;
	private LobbyActivity lobbyActivity;
	private Party party;

	private ClientGameHandler(Context context) {
		this.context = context;
		connection = new Connection(isSingleplayer(), context);
		startListeningForServermode();
		startListeningForMessages();
	}

	public static void init(Context context) {
		if (handler == null) {
			handler = new ClientGameHandler(context);
		}
	}

	public void startListeningForMessages() {
		listeningForMessages = true;

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
			while (listeningForMessages) {
				try {
					if (connection.isConnected) {
						Message m = (Message) connection.getInputStream().readObject();
						handleInput(m);
					}
				} catch (Exception e) {
					Log.d("CONNECTION", "Could not get the message from the server.");
					e.printStackTrace();
				}
			}
			}
		});
		thread.start();
	}

	/**
	 * Listen to the shared preferences if the servermode changes.
	 * If the servermode changes create a new connection.
	 */
	private void startListeningForServermode(){
		SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.sharedpref_servermodeinfo), MODE_PRIVATE);

		// Set up a listener for connection information.
		listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
			public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
				Log.d("CONNECTION", "Changing connection to: singleplayer=" + isSingleplayer());
				listeningForMessages = false;
				connection.stopConnection();
				connection = new Connection(isSingleplayer(), context);
				startListeningForMessages();
			}
		};

		prefs.registerOnSharedPreferenceChangeListener(listener);
	}

	/**
	 * Use the shared preferences to determine if the servermode is singleplayer.
	 * @return if the servermode is singleplayer.
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
	 * @author Steven Bronsveld and Bram Pulles
	 */
	public void handleInput(Message m) {
		switch (m.getType()){
			case "PartyJoinedMessage":
				partyJoinedMessage();
				break;
			case "Party":
				updatePartyMessage((Party)m);
				break;
			case "PlayerInfoMessage":
				playerInfoMessage();
				break;
			case "HeartbeatMessage":
				heartbeatMessage();
				break;
		}
	}

	/**
	 *
	 * @author Bram Pulles
	 */
	private void heartbeatMessage(){
		connection.setLastHeartbeat(System.currentTimeMillis());
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
	 */
	private void updatePartyMessage(Party party){
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
	public void playerInfoMessage(){
		SharedPreferences sharedPrefs = context.getSharedPreferences(context.getString(R.string.sharedpref_playerinfo), MODE_PRIVATE);
		String username = sharedPrefs.getString("username", "-");
		if(!username.equals("-"))
			handler.connection.sendMessage(new PlayerInfoMessage(username));
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

	public Player getPlayer(){
		return player;
	}

  public Party getParty() {
		return party;
  }
}
