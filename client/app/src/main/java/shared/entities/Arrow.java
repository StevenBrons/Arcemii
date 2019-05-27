package shared.entities;

import java.util.ArrayList;
import java.util.List;

import client.view.RenderItem;

/**
 * An arrow projectile shot by e.g. a Skeleton
 * @author Jelmer Firet
 */
public class Arrow extends Entity{
	private int xPos,yPos;
	private int xVel,yVel;

	/**
	 * Initialises an arrow
	 * @param x the x position of the center of the arrow (game pixels)
	 * @param y the y position of the center of the arrow (game pixels)
	 * @param dx the velocity in the x direction (game pixels)
	 * @param dy the velocity in the y direction (game pixels)
	 * @author Jelmer Firet
	 */
	public Arrow(int x,int y, int dx, int dy){
		this.xPos = x;
		this.yPos = y;
		this.xVel = dx;
		this.yVel = dy;
	}

	/**
	 * @return the RenderItem for this arrow, pointed in the direction of the arrow
	 * @author Jelmer Firet
	 */
	@Override
	public List<RenderItem> getRenderItem(){
		List<RenderItem> result = new ArrayList<>();
		RenderItem renderItem = new RenderItem("arrowHorizontal",xPos,yPos,0.5,0.5);
		renderItem.setRotation(180.0f/(float)Math.PI*(float)Math.atan2((float)yVel,(float) xVel));
		result.add(renderItem);
		return result;
	}
}
