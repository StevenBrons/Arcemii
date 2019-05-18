package server.general;

import java.util.ArrayList;

import shared.entities.Player;
import shared.messages.JoinPartyMessage;
import shared.messages.LeavePartyMessage;
import shared.messages.Message;
import shared.messages.PartyJoinedMessage;

public class ServerGameHandler {

	private ArrayList<Party> parties = new ArrayList<>();

	public ServerGameHandler() {
	}

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
		}
	}

	/**
	 * Remove the player from his party.
	 * @param m leave party message.
	 * @param player
	 * @author Bram Pulles
	 */
	private void leavePartyMessage(LeavePartyMessage m, Player player){
		for(Party party : parties) {
			if (party.containsPlayer(player))
				party.removePlayer(player);
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
				party.addPlayer(player);
				player.sendMessage(new PartyJoinedMessage());
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
