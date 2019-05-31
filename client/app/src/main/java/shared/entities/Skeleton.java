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
	private boolean shooting;

	/**
	 * Constructs a new skeleton
	 * @param x the x position of the feet of the skeleton (game pixels)
	 * @param y the y position of the feet of the skeleton (game pixels)
	 * @author Jelmer Firet
	 */
	public Skeleton(double x,double y){
		super(x,y);
	}

	/**
	 * Sets the velocity of the skeleton
	 * @param dx the x velocity of the skeleton (game pixels)
	 * @param dy the y velocity of the skeleton (game pixels)
	 * @author Jelmer Firet
	 */
	public void setVelocity(double dx, double dy){
		this.xVel = dx;
		this.yVel = dy;
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
			renderItem = new RenderItem("skeleton/skeletonShooting",
					(int)(Tile.WIDTH*xPos), -(int)(Tile.HEIGHT*yPos),0.5,1.0,2);
		}
		else if (xVel*xVel+yVel*yVel>5){
			renderItem = new RenderItem("skeleton/skeletonWalking",
					(int)(Tile.WIDTH*xPos), -(int)(Tile.HEIGHT*yPos),0.5,1.0,2);
		}
		else{
			renderItem = new RenderItem("skeleton/skeletonIdle",
					(int)(Tile.WIDTH*xPos), -(int)(Tile.HEIGHT*yPos),0.5,1.0,2);
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
