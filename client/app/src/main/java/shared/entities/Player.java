package shared.entities;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import client.view.RenderItem;
import shared.general.Level;
import shared.messages.Message;
import shared.tiles.Tile;

/**
 * The class that handles rendering and actions of Sako, the player character
 */
public class Player extends Entity{

	private String name = "Player#" + (int)(Math.random()*99999);
	private int xPos, yPos;

	public Player(int x, int y){
		this.xPos = x;
		this.yPos = y;
	}

	@Override
	public RenderItem getRenderItem(){
		return new RenderItem("playerBlueIdle",xPos, yPos,0.5,1.0);
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

	public int getxPos() {
		return xPos;
	}

	public int getyPos() {
		return yPos;
	}
}
