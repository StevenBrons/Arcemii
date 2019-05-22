package shared.entities;

import shared.entities.Entity;
import client.view.RenderItem;
import shared.tiles.Tile;

public class Slime extends Entity {
	private int xPos,yPos;
	private int xVel,yVel;

	public Slime(int x,int y){
		this.xPos = x;
		this.yPos = y;
	}

	public void setVelocity(int dx, int dy){
		this.xVel = dx;
		this.yVel = dy;
	}

	@Override
	public RenderItem getRenderItem(){
		if (xVel*xVel+yVel*yVel > 5){
			return new RenderItem("slime/redSlimeJump",xPos, yPos,0.5,1.0);
		}
		else{
			return new RenderItem("slime/testSlime",xPos, yPos,0.5,1.0);
		}
	}
}
