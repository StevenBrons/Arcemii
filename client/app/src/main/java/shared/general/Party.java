package shared.general;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import server.generator.Generator;
import shared.entities.Entity;
import shared.entities.Player;
import shared.messages.GameUpdateMessage;
import shared.messages.Message;

public class Party extends Message {

	private Lock levelLock = new ReentrantLock();
	private transient Level curLevel;
	private int partyId;
	private List<Player> players = Collections.synchronizedList(new ArrayList<>());
	public transient boolean inLobby = true;

	/**
	 * Creates a new party
	 */
	public Party() {
		partyId = (int)((Math.random()*8999) + 1000);
	}

	/**
	 * Add a player to the party
	 * @param player The player that will be added
	 */
	public void addPlayer(Player player) {
		if (!players.contains(player)) {
			player.setColor(players.size() % 4);
			players.add(player);
		}
		messageAll(this);
	}

	/**
	 * Remove the player from the party.
	 * @param player
	 * @author Bram Pulles
	 */
	public void removePlayer(Player player) {
		if(players.contains(player)){
			players.remove(player);
		}
		messageAll(this);
	}

	/**
	 * Send a message to all players (clients) that are in this party
	 * @param message The message to send, should not be null
	 */
	public void messageAll(Message message) {
		synchronized (players) {
			for (Player p : players) {
				p.sendMessage(message);
			}
		}
	}

	/**
	 *
	 * @return The party Id
	 */
	public int getPartyId() {
		return partyId;
	}

	/**
	 * @param player
	 * @return if the party contains the player.
	 */
	public boolean containsPlayer(Player player){
		return players.contains(player);
	}

	public boolean isEmpty(){
		return players.size() == 0;
	}

	public List<Player> getPlayers() {
		return players;
	}


	/**
	 * Start the game of the party, it generates a new level, adds the players to this level and messages all player this new level
	 * @author Steven Bronsveld
	 */
	public void startGame() {
		levelLock.lock();
		Generator g = new Generator(22,22,2,2,5,3);
		curLevel  = g.generate(6);

		synchronized (players) {
			for (Player p : players) {
				curLevel.spawnPlayer(p);
			}
		}

		messageAll(curLevel);
		this.inLobby = false;
		levelLock.unlock();
	}

	/**
	 * Update party, if the players are in the lobby a heartbeat message (an empty GameUpdateMessage) is sent, otherwise the party is updated by invoking all abilities of the non-player entities, it than executes the actions of all entities. It respawns dead players and sends game-updates to all party-members
	 * @author Steven Bronsveld
	 */
	public void update() {
		if (!inLobby) {
			levelLock.lock();
			curLevel.invoke();
			curLevel.execute();
			curLevel.respawnDeadPlayers();
			ArrayList<Entity> changes = curLevel.getChanges();
			curLevel.removeDead();
			levelLock.unlock();
			messageAll(new GameUpdateMessage(changes));
		} else {
			messageAll(new GameUpdateMessage(new ArrayList<>()));
		}
	}

	/**
	 * @return if everyone from the party is ready.
	 * @author Bram Pulles
	 */
	public boolean everyoneReady(){
		synchronized (players) {
			for (Player player : players) {
				if (!player.isReady())
					return false;
			}
		}
		return true;
	}


}
