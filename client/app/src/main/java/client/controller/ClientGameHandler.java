package client.controller;

import client.activities.JoinPartyActivity;
import shared.messages.Message;

public class ClientGameHandler {

	public static ClientGameHandler handler;
	public Connection connection;

	private JoinPartyActivity joinPartyActivity;

	public ClientGameHandler() {
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

	public void handleInput(Message m) {
		switch (m.getType()){
			case "PartyJoinedMessage":
				partyJoinedMessage();
				break;
		}
	}

	private void partyJoinedMessage(){
		joinPartyActivity.openLobby();
	}

	public void setJoinPartyActivity(JoinPartyActivity joinPartyActivity){
		this.joinPartyActivity = joinPartyActivity;
	}

}
