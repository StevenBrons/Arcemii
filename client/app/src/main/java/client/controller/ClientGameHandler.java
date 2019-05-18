package client.controller;

import client.activities.JoinPartyActivity;
import client.activities.LobbyActivity;
import shared.messages.Message;
import shared.messages.UpdatePartyMessage;

public class ClientGameHandler {

	public static ClientGameHandler handler;
	private Connection connection;

	private JoinPartyActivity joinPartyActivity;
	private LobbyActivity lobbyActivity;

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
		}
	}

	/**
	 * Open the lobby activity.
	 * @author Bram Pulles
	 */
	private void partyJoinedMessage(){
		joinPartyActivity.openLobby();
	}

	private void updatePartyMessage(UpdatePartyMessage m){
		lobbyActivity.updatePartyMessage(m);
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

}
