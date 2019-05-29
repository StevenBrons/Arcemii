package shared.entities;

import java.util.ArrayList;
import java.util.List;

import client.view.RenderItem;
import shared.general.Level;
import shared.tiles.Tile;

/**
 * Class that handles actions and rendering of a Skeleton
 * @author Jelmer Firet
 */
public class Skeleton extends Entity {

	/**
	 * Constructs a new skeleton
	 * @param x the x position of the feet of the skeleton (game pixels)
	 * @param y the y position of the feet of the skeleton (game pixels)
	 * @author Jelmer Firet
	 */
	public Skeleton(int x,int y){
		this.xPos = x;
		this.yPos = y;
	}

	/**
	 * Sets whether this skeleton is shooting an arrow
	 * @param shooting describes if the skeleton is shooting
	 * @author Jelmer Firet
	 */
	public void setShooting(boolean shooting){
		this.shooting = shooting;
	}

	/**
	 * @return the RenderItem associated with the current state of this skeleton
	 * @author Jelmer Firet
	 */
	@Override
	public List<RenderItem> getRenderItem(){
		List<RenderItem> result = new ArrayList<>();
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
