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

/**
 * The class that handles rendering and actions of Sako, the player character
 */
public class Player extends Entity {

	private String name = "";
	private int xPos, yPos;
	private int xVel, yVel;
	private int color;

	private transient ObjectInputStream input;
	private transient ObjectOutputStream output;

	public Player(ObjectInputStream input, ObjectOutputStream output) {
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

	public Player(int x, int y, int color){
		this.xPos = x;
		this.yPos = y;
		this.color = color;
	}

	public void setVelocity(int dx, int dy){
		this.xVel = dx;
		this.yVel = dy;
	}

	@Override
	public List<RenderItem> getRenderItem(){
		List<RenderItem> result = new ArrayList<>();
		RenderItem renderItem;
		if (xVel*xVel+yVel*yVel>5){
			switch (color) {
				case 0: renderItem = new RenderItem(
						"player/playerBlueWalking", xPos, yPos, 0.5, 1.0);break;
				case 1: renderItem = new RenderItem(
						"player/playerGreenWalking", xPos, yPos, 0.5, 1.0);break;
				case 2: renderItem = new RenderItem(
						"player/playerGreyWalking", xPos, yPos, 0.5, 1.0);break;
				case 3: renderItem = new RenderItem(
						"player/playerRedWalking", xPos, yPos, 0.5, 1.0);break;
				default: renderItem = new RenderItem(
						"player/playerBlueWalking", xPos, yPos, 0.5, 1.0);break;
			}
		}
		else{
			switch (color) {
				case 0: renderItem = new RenderItem(
						"player/playerBlueIdle", xPos, yPos, 0.5, 1.0);break;
				case 1: renderItem = new RenderItem(
						"player/playerGreenIdle", xPos, yPos, 0.5, 1.0);break;
				case 2: renderItem = new RenderItem(
						"player/playerGreyIdle", xPos, yPos, 0.5, 1.0);break;
				case 3: renderItem = new RenderItem(
						"player/playerRedIdle", xPos, yPos, 0.5, 1.0);break;
				default: renderItem = new RenderItem(
						"player/playerBlueIdle", xPos, yPos, 0.5, 1.0);break;
			}
		}
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

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}

}
