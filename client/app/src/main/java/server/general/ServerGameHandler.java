package server.general;

import java.util.ArrayList;

import shared.entities.Player;
import shared.general.Party;
import shared.messages.ActionMessage;
import shared.messages.JoinPartyMessage;
import shared.messages.Message;
import shared.messages.PartyJoinedMessage;
import shared.messages.PlayerInfoMessage;

public class ServerGameHandler {

	private ArrayList<Party> parties = new ArrayList<>();
	public static final int TICKSPEED = 100;

	private boolean running;

	public ServerGameHandler() {
		running = true;

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (isRunning()) {
					long start = System.currentTimeMillis();
					for (Party p : parties) {
						p.update();
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
	 * @author Steven Bronsveld and Bram Pulles
	 */
	public void addPlayer(final Player player) {
		System.out.println("A new player has joined: " + player.getName());

		// Put the player in an initial party.
		createParty(player);

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
				System.out.println("A player has been removed: " + player.getName());
			}
		}).start();

		player.sendMessage(new PlayerInfoMessage(player));
	}

	/**
	 * Handles the message send by a certain client.
	 * @param m message.
	 * @param player
	 * @author Steven Bronsveld and Bram Pulles
	 */
	private void handlePlayerInput(Message m, Player player) {
		Console.log(ConsoleTag.CONNECTION,m,player);
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
		if(m.getName().length() > 0) {
			player.setName(m.getName());
		}
	}

	/**
	 * Remove the player from his party.
	 * @param player
	 * @author Bram Pulles
	 */
	private void leavePartyMessage(Player player){
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
		for(int i = parties.size() -1; i >= 0; i--) {
			Party party = parties.get(i);
			if (party.containsPlayer(player)) {
				party.removePlayer(player);
				if(party.isEmpty())
					parties.remove(party);
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
		for(Party party : parties){
			if(party.getPartyId() == m.getPartyId()){
				leaveParty(player);

				party.addPlayer(player);
				player.sendMessage(new PartyJoinedMessage());
			}
		}
	}

	private void startGameMessage(Player player) {
		for (Party party : parties) {
			if (party.containsPlayer(player)) {
				party.startGame();
				return;
			}
		}
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

		for(Party party : parties){
			for(Player player : party.getPlayers()){
				player.stop();
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
		return parties;
	}

}
