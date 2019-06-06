package shared.entities;

import java.util.ArrayList;
import java.util.List;

import client.view.RenderItem;
import shared.abilities.Melee;
import shared.general.Level;
import shared.tiles.Tile;

/**
 * An arrow projectile shot by e.g. a Skeleton
 * @author Jelmer Firet
 */
public class Arrow extends Entity{

	Melee melee = new Melee();
	boolean hitplayer;

	/**
	 * Initialises an arrow
	 * @param x the x position of the center of the arrow (tiles)
	 * @param y the y position of the center of the arrow (tiles)
	 * @param dx the velocity in the x direction (tiles)
	 * @param dy the velocity in the y direction (tiles)
	 * @author Jelmer Firet
	 */
	public Arrow(double x,double y, double dx, double dy,boolean hitplayer){
		super(x,y);
		this.xVel = dx;
		this.yVel = dy;
		this.hitplayer = hitplayer;
	}

	/**
	 * @return the RenderItem for this arrow, pointed in the direction of the arrow
	 * @author Jelmer Firet
	 */
	@Override
	public List<RenderItem> getRenderItem(){
		List<RenderItem> result = new ArrayList<>();
		RenderItem renderItem = new RenderItem("arrowHorizontal",
				(int)(Tile.WIDTH*xPos),-(int)(Tile.HEIGHT*yPos),0.5,0.5,2);
		renderItem.setRotation(180.0f/(float)Math.PI*(float)Math.atan2((float)-yVel,(float) xVel));
		result.add(renderItem);
		return result;
	}

	@Override
	public void invokeAll(Level level) {
		this.actions.clear();
		boolean intersects = false;
		for (int i = 0;i<level.getNumEntity();i++){
			Entity entity = level.getEntityAt(i);
			if (this.intersects(entity) && this != entity){
				intersects = true;
			}
		}
		if (intersects && this.melee.available(level,this)){
			invoke(this.melee.invoke(hitplayer,2));
			this.destroy();
		}
		if (!this.move.available(level,this)){
			this.destroy();
		}
		invoke(this.move.invoke(Math.atan2(yVel,xVel)));
	}


}
