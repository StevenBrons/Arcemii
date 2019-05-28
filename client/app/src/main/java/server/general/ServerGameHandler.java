package server.general;

import java.util.ArrayList;

import shared.entities.Player;
import shared.general.Party;
import shared.messages.HeartbeatMessage;
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
	 * @param player
	 * @author Steven Bronsveld and Bram Pulles
	 */
	public void addPlayer(final Player player) {
		System.out.println("A new client has joined: " + player.getName());
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
			while (true) {
				try {
					Message m = (Message) player.getInputStream().readObject();
					handlePlayerInput(m, player);
				} catch (Exception e) {
				}
			}
			}
		});
		thread.start();

		player.sendMessage(new PlayerInfoMessage());
	}

	/**
	 * Handles the message send by a certain client.
	 * @param m message.
	 * @param player
	 * @author Steven Bronsveld and Bram Pulles
	 */
	private void handlePlayerInput(Message m, Player player) {
		switch (m.getType()) {
			case "CreatePartyMessage":
				createPartyMessage(player);
				break;
			case "JoinPartyMessage":
				joinPartyMessage((JoinPartyMessage)m, player);
				break;
			case "LeavePartyMessage":
				leavePartyMessage(player);
				break;
			case "PlayerInfoMessage":
				playerInfoMessage((PlayerInfoMessage)m, player);
				break;
			case "HeartbeatMessage":
				heartbeatMessage(player);
				break;
		}
	}

	/**
	 * Send a heartbeat to the client.
	 * @param player
	 * @author Bram Pulles
	 */
	private void heartbeatMessage(Player player){
		player.sendMessage(new HeartbeatMessage());
	}

	/**
	 * This method receives information about the client on the client side and sets a new name for the client.
	 * @param m client info message.
	 * @param player
	 * @author Bram Pulles
	 */
	private void playerInfoMessage(PlayerInfoMessage m, Player player){
		if(m.getName().length() > 0)
			player.setName(m.getName());
	}

	/**
	 * Remove the client from his party.
	 * Also remove the party if the party is empty.
	 * @param client
	 * @author Bram Pulles
	 */
	private void leavePartyMessage(Player client){
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
	 * @param player
	 * @author Bram Pulles
	 */
	private void joinPartyMessage(JoinPartyMessage m, Player player){
		for(Party party : parties){
			if(party.getPartyId() == m.getPartyId()){
				player.sendMessage(new PartyJoinedMessage());
				party.addPlayer(player);
			}
		}
	}

	/**
	 * Create a new party.
	 * @param player
	 * @author Bram Pulles
	 */
	private void createPartyMessage(Player player){
		Party party = new Party();
		party.addPlayer(player);
		parties.add(party);
	}
}
