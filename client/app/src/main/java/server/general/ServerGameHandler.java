package server.general;

import java.util.ArrayList;

import shared.entities.Player;
import shared.messages.JoinPartyMessage;
import shared.messages.LeavePartyMessage;
import shared.messages.Message;

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

	private void leavePartyMessage(LeavePartyMessage m, Player player){
		for(Party party : parties) {
			if (party.containsPlayer(player))
				party.removePlayer(player);
		}
	}

	private void joinPartyMessage(JoinPartyMessage m, Player player){
		for(Party party : parties){
			if(party.getPartyId() == m.getPartyId())
				party.addPlayer(player);
		}
	}

	private void createPartyMessage(Player player){
		Party party = new Party();
		party.addPlayer(player);
		parties.add(party);
	}
}
