package shared.entities;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import client.view.RenderItem;
import shared.general.Level;
import shared.messages.Message;
import shared.tiles.Tile;

/**
 * The class that handles rendering and actions of Player, the player character
 */
public class Player extends Entity{
	private int xPos, yPos;

	public Player(int x, int y){
		this.xPos = x;
		this.yPos = y;
	}

	@Override
	public RenderItem getRenderItem(){
		return new RenderItem("playerBlueIdle",xPos+Tile.WIDTH/2, yPos+Tile.HEIGHT,0.5,1.0);
	}
}
