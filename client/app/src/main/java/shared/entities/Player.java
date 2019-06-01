package shared.entities;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import client.view.RenderItem;
import shared.abilities.Ability;
import shared.general.Level;
import shared.messages.Message;
import shared.tiles.Tile;

/**
 * The class that handles rendering and actions of Sako, the player character
 * @author Jelmer Firet
 */
public class Player extends Entity {

	private String name = "Player#" + (int)(Math.random()*99999);
	private int color;

	private transient ObjectInputStream input;
	private transient ObjectOutputStream output;

	public Player(ObjectInputStream input, ObjectOutputStream output) {
		super(0,0);
		this.input = input;
		this.output = output;
	}

	/**
	 * Send message to this client.
	 * @param m message
	 */
	public void sendMessage(Message m) {
		try {
			output.writeObject(m);
			output.flush();
			output.reset();
		} catch (IOException e) {
		}
	}
	public ObjectInputStream getInputStream() {
		return input;
	}

	/**
	 * Initialises the Player class
	 * @param x the x position of the feet of the player (game pixels)
	 * @param y the y position of the feet of the player (game pixels)
	 * @param color the color of the player's hat: 0=blue,1=green,2=grey,3=red
	 * @author Jelmer Firet
	 */
	public Player(double x, double y, int color){
		super(x,y);
		this.color = color;
	}

	/**
	 * @return the render item associated with this player
	 * @author Jelmer Firet
	 */
	@Override
	public List<RenderItem> getRenderItem(){
		List<RenderItem> result = new ArrayList<>();
		String textureName;
		if (xVel*xVel+yVel*yVel>5){
			switch (color) {
				case 0: textureName = "player/playerBlueWalking";break;
				case 1: textureName = "player/playerGreenWalking";break;
				case 2: textureName = "player/playerGreyWalking";break;
				case 3: textureName = "player/playerRedWalking";break;
				default: textureName = "player/playerBlueWalking";break;
			}
		}
		else{
			switch (color) {
				case 0: textureName = "player/playerBlueIdle";break;
				case 1: textureName = "player/playerGreenIdle";break;
				case 2: textureName = "player/playerGreyIdle";break;
				case 3: textureName = "player/playerRedIdle";break;
				default: textureName = "player/playerBlueIdle";break;
			}
		}
		RenderItem renderItem = new RenderItem(textureName,
				(int)(Tile.WIDTH*xPos),-(int)(Tile.HEIGHT*yPos),0.5,1.0,2);
		if (xVel < 0){
			renderItem.setFlip(true);
		}
		result.add(renderItem);
		return result;
	}

	@Override
	public void invokeAll(Level level) {
		// the abilities are invoked by the client!
	}

	@Override
	public boolean update(Level level) {
		return false;
	}

	public ArrayList<Ability> getActions() {
		return actions;
	}

	/**
	 * Set the name of the player.
	 * @param name
	 * @author Bram Pulles
	 */
	public void setName(String name){
		this.name = name;
	}

	/**
	 * @return name of the player.
	 * @author Bram Pulles
	 */
	public String getName(){
		return name;
	}

}
