package shared.general;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import server.generator.Generator;
import shared.entities.Player;
import shared.messages.GameUpdateMessage;
import shared.messages.Message;

public class Party extends Message {

	private Lock levelLock = new ReentrantLock();
	private transient Level curLevel;
	private int partyId;
	private ArrayList<Player> players;
	public transient boolean inLobby = true;

	public Party() {
		partyId = (int)(Math.random()*99999);
		players = new ArrayList<>();
	}

	public void addPlayer(Player client) {
		if (!players.contains(client)) {
			players.add(client);
		}
		messageAll(this);
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
		messageAll(this);
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

	public boolean isEmpty(){
		return players.size() == 0;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}


	public void startGame() {
		Generator g = new Generator(22,22,2,2,5,3);
		curLevel  = g.generate(6);

		for (Player p : players) {
			curLevel.spawnPlayer(p);
		}

		messageAll(curLevel);
		this.inLobby = false;
	}

	public void update() {
		if (!inLobby) {
			levelLock.lock();
			curLevel.invoke();
			curLevel.execute();
			levelLock.unlock();
		}
		sendPlayers(new GameUpdateMessage(curLevel.getChanges()));
	}

	private void sendPlayers(Message m){
		for (Player p : players) {
			p.sendMessage(m);
		}
	}


}
