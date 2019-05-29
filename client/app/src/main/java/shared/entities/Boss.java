package shared.entities;

import java.util.ArrayList;
import java.util.List;

import client.view.RenderItem;
import shared.tiles.Tile;

/**
 * class that handles actions and rendering of the boss mob
 * @author Jelmer Firet
 */
public class Boss extends Entity {
	private double xPos,yPos;

	/**
	 * Initialises the boss mob
	 * @param x the x position of the feet of the Boss (game pixels)
	 * @param y the y position of the feet of the Boss (game pixels)
	 */
	public Boss(double x, double y){
		this.xPos = x;
		this.yPos = y;
	}

  @Override
	public List<RenderItem> getRenderItem(){
		List<RenderItem> result = new ArrayList<>();
		result.add(new RenderItem("boss/bossFloating",
				(int)(Tile.WIDTH*xPos), (int)(Tile.HEIGHT*yPos),0.5,1.0));
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
