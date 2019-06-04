package shared.entities;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import client.view.RenderItem;
import shared.abilities.Ability;
import shared.abilities.Move;
import shared.general.Level;
import shared.messages.Message;
import shared.tiles.Tile;

public class Player extends Entity {

	private String name = "Player#" + (int)(Math.random()*99999);
	private int color;

	//server only
	private transient boolean unique = true;
	private transient InetAddress ip;
	private transient ObjectInputStream input;
	private transient ObjectOutputStream output;

	//client only
	public static transient double direction = 0;
	public static transient boolean move = false;

	public Player(InetAddress ip, ObjectInputStream input, ObjectOutputStream output) {
		super(0,0);
		this.input = input;
		this.output = output;
		this.ip = ip;
	}

	/**
	 * Send message to this client.
	 * @param m message
	 */
	public synchronized void sendMessage(Message m) {
		try {
			output.writeObject(m);
			output.flush();
			output.reset();
		} catch (IOException e) {
//			e.printStackTrace();
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
		maxhealth = health = 50;
	}

	/**
	 * @return the render item associated with this player
	 * @author Jelmer Firet
	 */
	@Override
	public List<RenderItem> getRenderItem(){
		List<RenderItem> result = new ArrayList<>();
		String textureName;
		if (xVel*xVel+yVel*yVel>0.005){
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

	public synchronized void invokeBottom() {
		invoke(abilities.get(1));
	}

	public synchronized void invokeMiddle() {
		invoke(abilities.get(2));
	}

	public synchronized void invokeUpper() {
		invoke(abilities.get(3));
	}

	public synchronized void invokeMove() {
	  if (move) {
      invoke(((Move)abilities.get(0)).invoke(direction));
    }
	}

	public synchronized void setAbilities(ArrayList<Ability> abilities) {
		this.abilities = abilities;
	}

  public synchronized void clearActions() {
		if (actions != null) {
			actions.clear();
		}
  }

	public synchronized void setActions(ArrayList<Ability> actions) {
	  this.actions = actions;
  }

	@Override
	public String toString() {
		return getName();
	}

	/**
	 * @return the ip of the players device.
	 * @author Bram Pulles
	 */
	public InetAddress getIp(){
		return ip;
	}

	/**
	 * Set the player to not unique.
	 * This should be based on the ip address.
	 * @author Bram Pulles
	 */
	public void setNotUnique(){
		unique = false;
	}

	/**
	 * @return if the player is unique, based on the ip address.
	 * @author Bram Pulles
	 */
	public boolean isUnique(){
		return unique;
	}

	/**
	 * Close the streams used by this player.
	 * @author Bram Pulles
	 */
	public void stop(){
		try{
			output.close();
		}catch(Exception e){
			e.printStackTrace();
		}

		try{
			input.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
