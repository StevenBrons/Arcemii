package shared.entities;

import client.view.RenderItem;
import shared.tiles.Tile;

public class Skeleton extends Entity {
	private int xPos,yPos;
	private int xVel,yVel;
	private boolean shooting;

	public Skeleton(int x,int y){
		this.xPos = x;
		this.yPos = y;
	}

	public void setVelocity(int dx, int dy){
		this.xVel = dx;
		this.yVel = dy;
	}

	public void setShooting(boolean shooting){
		this.shooting = shooting;
	}

	@Override
	public RenderItem getRenderItem(){
		RenderItem renderItem;
		if (shooting){
			renderItem = new RenderItem("skeleton/skeletonShooting",xPos, yPos,0.5,1.0);
		}
		else if (xVel*xVel+yVel*yVel>5){
			renderItem = new RenderItem("skeleton/skeletonWalking",xPos, yPos,0.5,1.0);
		}
		else{
			renderItem = new RenderItem("skeleton/skeletonIdle",xPos, yPos,0.5,1.0);
		}
		if (xVel < 0){
			renderItem.setFlip(true);
		}
		return renderItem;
	}
}
