package shared.entities;

import java.util.ArrayList;
import java.util.List;

import shared.entities.Entity;
import client.view.RenderItem;
import shared.general.Level;
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
	public List<RenderItem> getRenderItem(){
		List<RenderItem> result = new ArrayList<>();
		RenderItem renderItem;
		if (xVel*xVel+yVel*yVel > 5){
			renderItem = new RenderItem("slime/redSlimeJump",xPos, yPos,0.5,1.0);
		}
		else{
			renderItem = new RenderItem("slime/testSlime",xPos, yPos,0.5,1.0);
		}
		if (xVel < 0){
			renderItem.setFlip(true);
		}
		result.add(renderItem);
		return result;
	}

	@Override
	public void invokeAll(Level level) {
	}

	@Override
	public boolean update(Level level) {
		return false;
	}
}
