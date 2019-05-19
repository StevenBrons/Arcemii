package server.general;

import android.util.Log;

import java.util.ArrayList;

import shared.entities.Player;
import shared.general.Level;
import shared.messages.Message;
import shared.messages.UpdatePartyMessage;

public class Party {

	private Level curLevel;
	private int partyId = (int)(Math.random()*99999);
	private ArrayList<Player> players = new ArrayList<>();

	public Party () {
	}

	public void addPlayer(Player player) {
		if (!players.contains(player)) {
			players.add(player);
		}
		Log.d("SEND TO CLIENT", players.get(0).getName());
		messageAll(new UpdatePartyMessage(partyId, players));
	}

	/**
	 * Remove the player from the party.
	 * @param player
	 * @author Bram Pulles
	 */
	public void removePlayer(Player player){
		if(players.contains(player)){
			players.remove(player);
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

	public boolean containsPlayer(Player player){
		return players.contains(player);
	}

	public void setCurrentLevel(Level level){
		curLevel = level;
	}

	public boolean isEmpty(){
		return players.size() == 0;
	}

}
