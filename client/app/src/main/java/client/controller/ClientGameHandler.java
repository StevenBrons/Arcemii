package client.controller;

import android.content.Context;
import android.content.SharedPreferences;

import com.debernardi.archemii.R;

import client.activities.JoinPartyActivity;
import client.activities.LobbyActivity;
import shared.entities.Player;
import shared.messages.Message;
import shared.messages.PlayerInfoMessage;
import shared.messages.UpdatePartyMessage;

import static android.content.Context.MODE_PRIVATE;

public class ClientGameHandler {

	public static ClientGameHandler handler;
	private Connection connection;
	private Player player = new Player(36,48,0);

	private Context context;

	private JoinPartyActivity joinPartyActivity;
	private LobbyActivity lobbyActivity;
	private UpdatePartyMessage updatePartyMessage;

	private ClientGameHandler(Context context) {
		this.context = context;
		connection = new Connection(false, context);
		startListening();
	}

	public static void init(Context context) {
		if (handler == null) {
			handler = new ClientGameHandler(context);
		}
	}

	public void startListening() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
			while (true) {
				try {
					if (connection.isConnected) {
						Message m = (Message) connection.getInputStream().readObject();
						handleInput(m);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			}
		});
		thread.start();
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
			case "UpdatePartyMessage":
				updatePartyMessage((UpdatePartyMessage)m);
				break;
			case "PlayerInfoMessage":
				playerInfoMessage();
				break;
		}
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
	 * @param m update party message.
	 * @author Bram Pulles
	 */
	private void updatePartyMessage(UpdatePartyMessage m){
		updatePartyMessage = m;
		if(lobbyActivity != null)
			lobbyActivity.updatePartyMessage(m);
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

	/**
	 * @return the latest update party message.
	 */
	public UpdatePartyMessage getUpdatePartyMessage(){
		return updatePartyMessage;
	}

	public Player getPlayer(){
		return player;
	}

}
