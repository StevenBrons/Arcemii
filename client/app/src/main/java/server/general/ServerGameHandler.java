package server.general;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import shared.entities.Player;
import shared.general.Party;
import shared.messages.ActionMessage;
import shared.messages.JoinPartyMessage;
import shared.messages.Message;
import shared.messages.PartyJoinedMessage;
import shared.messages.PlayerInfoMessage;
import shared.messages.ReadyMessage;

public class ServerGameHandler {

	private List<Party> parties = Collections.synchronizedList(new ArrayList<>());
	public static final int TICKSPEED = 10;

	private boolean running;

	/**
	 * Run server game loop, updating all parties
	 * @author Steven Bronsveld
	 */
	public ServerGameHandler() {
		running = true;

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (isRunning()) {
					long start = System.currentTimeMillis();
					synchronized (parties){
						for (Party p : parties) {
							p.update();
						}
					}

					long timeTook = System.currentTimeMillis() - start;
					try {
						if (timeTook > TICKSPEED) {
							timeTook = TICKSPEED;
						}
						Thread.sleep(TICKSPEED - timeTook);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	/**
	 * Put the player in an initial party.
	 * Make a new thread to receive messages from the new client.
	 * Send a message to the client with the client information.
	 * @param player
	 * @author Steven Bronsveld
	 * @author Bram Pulles
	 */
	public void addPlayer(final Player player) {
		Console.log(ConsoleTag.CONNECTION, "has joined.", player);

		// Put the player in an initial party.
		createParty(player);
		player.sendMessage(new PlayerInfoMessage(player));

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (isRunning() && player.isUnique()) {
					try {
						Message m = (Message) player.getInputStream().readObject();
						handlePlayerInput(m, player);
					} catch (Exception e) {
					}
				}
				leaveParty(player);
				Console.log(ConsoleTag.CONNECTION, "has left.", player);
			}
		}).start();
	}

	/**
	 * Handles the message send by a certain client.
	 * @param m message.
	 * @param player
	 * @author Steven Bronsveld
	 * @author Bram Pulles
	 */
	private void handlePlayerInput(Message m, Player player) {
		Console.log(ConsoleTag.CONNECTION, m.toString(), player);

		switch (m.getType()) {
			case "CreatePartyMessage":
				createPartyMessage(player);
				break;
			case "JoinPartyMessage":
				joinPartyMessage((JoinPartyMessage) m, player);
				break;
			case "LeavePartyMessage":
				leavePartyMessage(player);
				break;
			case "PlayerInfoMessage":
				playerInfoMessage((PlayerInfoMessage) m, player);
				break;
			case "StartGameMessage":
				startGameMessage(player);
				break;
			case "ActionMessage":
				actionMessage((ActionMessage) m,player);
				break;
			case "ReadyMessage":
				readyMessage((ReadyMessage)m, player);
		}
	}

	private void readyMessage(ReadyMessage m, Player player){
		player.setReady(m.isReady());
		Console.log(ConsoleTag.CONNECTION, "ready: " + player.getName(), player);
		synchronized (parties){
			for(Party party : parties){
				if(party.containsPlayer(player)){
					party.messageAll(party);
				}
			}
		}
	}

	private void actionMessage(ActionMessage m, Player player) {
		player.setActions(m.getActions());
	}

	/**
	 * This method receives information about the client on the client side and sets a new name for the client.
	 * @param m client info message.
	 * @param player
	 * @author Bram Pulles
	 */
	private void playerInfoMessage(PlayerInfoMessage m, Player player){
		Console.log(ConsoleTag.CONNECTION, "Player info message received.", player);

		if(m.getName().length() > 0) {
			player.setName(m.getName());
		}
		if(m.getAbilities().size() > 0) {
			player.setAbilities(m.getAbilities());
		}
	}

	/**
	 * Remove the player from his party and put him in an empty one.
	 * Set the player to non ready.
	 * @param player
	 * @author Bram Pulles
	 */
	private void leavePartyMessage(Player player){
		player.setReady(false);
		leaveParty(player);
		createParty(player);
	}

	/**
	 * Remove the player from the party, if in any.
	 * And remove the party if the party is empty.
	 * @param player
	 * @author Bram Pulles
	 */
	private void leaveParty(Player player){
		synchronized (parties){
			for(int i = parties.size() -1; i >= 0; i--) {
				Party party = parties.get(i);
				if (party.containsPlayer(player)) {
					party.removePlayer(player);
					if(party.isEmpty())
						parties.remove(party);
				}
			}
		}
	}

	/**
	 * Join the client to the given party.
	 * \@param m join party message.
	 * @param player
	 * @author Bram Pulles
	 */
	private void joinPartyMessage(JoinPartyMessage m, Player player){
		synchronized (parties){
			for(Party party : parties){
				if(party.getPartyId() == m.getPartyId()){
					leaveParty(player);

					party.addPlayer(player);
					player.sendMessage(new PartyJoinedMessage());
				}
			}
		}
	}

	/**
	 * Set the master to ready and start a game for the given party if everyone is ready.
	 * @param player
	 * @author Bram Pulles
	 */
	private void startGameMessage(Player player) {
		// Set the master to ready.
		player.setReady(true);

		synchronized (parties){
			for (Party party : parties) {
				if (party.containsPlayer(player) && party.everyoneReady()) {
					party.startGame();
					return;
				}
			}
		}

		// If not everyone was ready
		player.setReady(false);
	}

	/**
	 * Create a new party and send this to the player.
	 * @param player
	 * @author Bram Pulles
	 */
	private void createPartyMessage(Player player){
		leaveParty(player);
		player.sendMessage(createParty(player));
	}

	/**
	 * Create a new party.
	 * @param player
	 * @return the party just created.
	 * @author Bram Pulles
	 */
	private Party createParty(Player player){
		Party party = new Party();
		party.addPlayer(player);
		parties.add(party);
		return party;
	}

	/**
	 * Stop the server game handler from sending and receiving messages.
	 * Also close all the connections with the players.
	 * @author Bram Pulles
	 */
	public synchronized void stop(){
		running = false;

		synchronized (parties){
			for(Party party : parties){
				for(Player player : party.getPlayers()){
					player.stop();
				}
			}
		}
	}

	/**
	 * @return if the server game handler is running.
	 * @author Bram Pulles
	 */
	public synchronized boolean isRunning(){
		return running;
	}

	/**
	 * @return a list of all the parties on the server.
	 * @author Bram Pulles
	 */
	public ArrayList<Party> getParties(){
		ArrayList<Party> result = new ArrayList<>(parties.size());
		synchronized (parties){
			Collections.copy(result, parties);
		}
		return result;
	}
}
