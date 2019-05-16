package shared.general;

import java.io.Serializable;
import java.util.ArrayList;

import shared.entities.Player;
import shared.messages.Message;
import shared.messages.UpdatePartyMessage;

public class Party implements Serializable {

	public static final long serialVersionUID = 1L;

	private int partyId = (int)Math.random()*99999;
	private ArrayList<Player> players = new ArrayList<>();

	public Party () {
	}

	public void addPlayer(Player player) {
		if (!players.contains(player)) {
			players.add(player);
		}
		messageAll(new UpdatePartyMessage(partyId, players));
	}

	public void messageAll(Message message) {
		for (Player p : players) {
			p.sendMessage(message);
		}
	}

	public int getPartyId() {
		return partyId;
	}
}
