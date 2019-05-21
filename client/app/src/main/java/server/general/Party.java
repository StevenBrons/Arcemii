package server.general;

import java.util.ArrayList;

import shared.general.Level;
import shared.messages.Message;
import shared.messages.UpdatePartyMessage;

public class Party {

	private Level curLevel;
	private int partyId = (int)(Math.random()*99999);
	private ArrayList<Client> clients = new ArrayList<>();

	public Party () {
	}

	public void addPlayer(Client client) {
		if (!clients.contains(client)) {
			clients.add(client);
		}
		messageAll(new UpdatePartyMessage(partyId, clients));
	}

	/**
	 * Remove the client from the party.
	 * @param client
	 * @author Bram Pulles
	 */
	public void removePlayer(Client client){
		if(clients.contains(client)){
			clients.remove(client);
		}
		messageAll(new UpdatePartyMessage(partyId, clients));
	}

	public void messageAll(Message message) {
		for (Client p : clients) {
			p.sendMessage(message);
		}
	}

	public int getPartyId() {
		return partyId;
	}

	public boolean containsPlayer(Client client){
		return clients.contains(client);
	}

	public void setCurrentLevel(Level level){
		curLevel = level;
	}

	public boolean isEmpty(){
		return clients.size() == 0;
	}

}
