package server.general;

import java.util.ArrayList;

import shared.messages.JoinPartyMessage;
import shared.messages.Message;
import shared.messages.PartyJoinedMessage;
import shared.messages.PlayerInfoMessage;

public class ServerGameHandler {

	private ArrayList<Party> parties = new ArrayList<>();

	public ServerGameHandler() {
	}

	/**
	 * Make a new thread to receive messages from the new client.
	 * Send a message to the client with the client information.
	 * @param client
	 * @author Steven Bronsveld and Bram Pulles
	 */
	public void addPlayer(final Client client) {
		System.out.println("A new client has joined: " + client);
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
			while (true) {
				try {
					Message m = (Message) client.getInputStream().readObject();
					handlePlayerInput(m, client);
				} catch (Exception e) {
				}
			}
			}
		});
		thread.start();

		client.sendMessage(new PlayerInfoMessage());
	}

	/**
	 * Handles the message send by a certain client.
	 * @param m message.
	 * @param client
	 * @author Steven Bronsveld and Bram Pulles
	 */
	private void handlePlayerInput(Message m, Client client) {
		switch (m.getType()) {
			case "CreatePartyMessage":
				createPartyMessage(client);
				break;
			case "JoinPartyMessage":
				joinPartyMessage((JoinPartyMessage)m, client);
				break;
			case "LeavePartyMessage":
				leavePartyMessage(client);
				break;
			case "PlayerInfoMessage":
				playerInfoMessage((PlayerInfoMessage)m, client);
				break;
		}
	}

	/**
	 * This method receives information about the client on the client side and sets a new name for the client.
	 * @param m client info message.
	 * @param client
	 * @author Bram Pulles
	 */
	private void playerInfoMessage(PlayerInfoMessage m, Client client){
		if(m.getName().length() > 0)
			client.setName(m.getName());
	}

	/**
	 * Remove the client from his party.
	 * Also remove the party if the party is empty.
	 * @param client
	 * @author Bram Pulles
	 */
	private void leavePartyMessage(Client client){
		for(Party party : parties) {
			if (party.containsPlayer(client)) {
				party.removePlayer(client);
				if(party.isEmpty())
					parties.remove(party);
			}
		}
	}

	/**
	 * Join the client to the given party.
	 * @param m join party message.
	 * @param client
	 * @author Bram Pulles
	 */
	private void joinPartyMessage(JoinPartyMessage m, Client client){
		for(Party party : parties){
			if(party.getPartyId() == m.getPartyId()){
				client.sendMessage(new PartyJoinedMessage());
				party.addPlayer(client);
			}
		}
	}

	/**
	 * Create a new party.
	 * @param client
	 * @author Bram Pulles
	 */
	private void createPartyMessage(Client client){
		Party party = new Party();
		party.addPlayer(client);
		parties.add(party);
	}
}
