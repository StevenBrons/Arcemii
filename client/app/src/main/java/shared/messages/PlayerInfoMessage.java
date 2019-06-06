package shared.messages;

import java.util.ArrayList;

import shared.abilities.Ability;
import shared.entities.Player;

/**
 * This class can be used to send a new name for a player to the server.
 * Or to request a name by the server during connection initialization.
 * @author Bram Pulles
 */
public class PlayerInfoMessage extends Message {

	private String name;
	private Player player;
	private ArrayList<Ability> abilities;

	public PlayerInfoMessage(Player player){
		this.name = player.getName();
		this.abilities = player.getAbilities();
		this.player = player;
	}

	public String getName() {
		return name;
	}

	public Player getPlayer() {
		return player;
	}

	public ArrayList<Ability> getAbilities() {
		return abilities;
	}

	@Override
	public String toString() {
		return "playerInfo(" + name + "," + abilities + ")";
	}
}
