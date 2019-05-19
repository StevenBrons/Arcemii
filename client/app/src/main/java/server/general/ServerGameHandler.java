package server.general;

import java.util.ArrayList;

import shared.entities.Player;
import shared.messages.JoinPartyMessage;
import shared.messages.LeavePartyMessage;
import shared.messages.Message;
import shared.messages.PartyJoinedMessage;
import shared.messages.PlayerInfoMessage;

public class ServerGameHandler {

	private ArrayList<Party> parties = new ArrayList<>();

	public ServerGameHandler() {
	}

	/**
	 * Make a new thread to receive messages from the new player.
	 * Send a message to the client with the player information.
	 * @param player
	 * @author Steven Bronsveld and Bram Pulles
	 */
	public void addPlayer(final Player player) {
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
	 * Handles the message send by a certain player.
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
				leavePartyMessage((LeavePartyMessage)m, player);
				break;
			case "PlayerInfoMessage":
				playerInfoMessage((PlayerInfoMessage)m, player);
				break;
		}
	}

	/**
	 * This method receives information about the player on the client side and sets a new name for the player.
	 * @param m player info message.
	 * @param player
	 * @author Bram Pulles
	 */
	private void playerInfoMessage(PlayerInfoMessage m, Player player){
		if(m.getName().length() > 0)
			player.setName(m.getName());
	}

	/**
	 * Remove the player from his party.
	 * Also remove the party if the party is empty.
	 * @param m leave party message.
	 * @param player
	 * @author Bram Pulles
	 */
	private void leavePartyMessage(LeavePartyMessage m, Player player){
		for(Party party : parties) {
			if (party.containsPlayer(player)) {
				party.removePlayer(player);
				if(party.isEmpty())
					parties.remove(party);
			}
		}
	}

	/**
	 * Join the player to the given party.
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
