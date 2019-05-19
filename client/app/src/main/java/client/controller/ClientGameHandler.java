package client.controller;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import client.activities.JoinPartyActivity;
import client.activities.LobbyActivity;
import shared.messages.Message;
import shared.messages.PlayerInfoMessage;
import shared.messages.UpdatePartyMessage;

public class ClientGameHandler extends AppCompatActivity {

	public static ClientGameHandler handler;
	private Connection connection;

	private JoinPartyActivity joinPartyActivity;
	private LobbyActivity lobbyActivity;
	private UpdatePartyMessage updatePartyMessage;

	private ClientGameHandler() {
		connection = new Connection(true);
		start();
	}

	public static void init() {
		if (handler == null) {
			handler = new ClientGameHandler();
		}
	}

	public void start() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
			while (true) {
				try {
					Message m = (Message) connection.getInputStream().readObject();
					handleInput(m);
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
		lobbyActivity.updatePartyMessage(m);
	}

	/**
	 * This function is called when the server and client established a connection
	 * and the player is created on the server. The player can send his custom
	 * name to the server.
	 * @author Bram Pulles
	 */
	public void playerInfoMessage(){
		Log.d("USERNAME", getString(R.string.sharedpref_filename));
		SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sharedpref_filename), MODE_PRIVATE);
		String username = sharedPreferences.getString(getString(R.string.sharedpref_username), "-");
		Log.d("USERNAME", username);
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

}
