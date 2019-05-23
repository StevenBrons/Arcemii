package shared.messages;

import java.util.ArrayList;

import server.general.Client;

public class UpdatePartyMessage extends Message{

	private int partyId;
	private ArrayList<Client> clients;

	public UpdatePartyMessage(int partyId, ArrayList<Client> clients) {
		this.partyId = partyId;
		this.clients = clients;
	}

	public int getPartyId() {
		return partyId;
	}

	public ArrayList<Client> getClients() {
		return clients;
	}
}
