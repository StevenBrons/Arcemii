package shared.messages;

import shared.entities.Player;

/**
 * This class can be used to send a new name for a player to the server.
 * Or to request a name by the server during connection initialization.
 * @author Bram Pulles
 */
public class PlayerInfoMessage extends Message {

	private String name;
	private Player player;

	public PlayerInfoMessage(Player player){
		this.name = player.getName(); this.player = player;
	}

	public String getName() {
		return name;
	}

	public Player getPlayer() {
		return player;
	}
}
