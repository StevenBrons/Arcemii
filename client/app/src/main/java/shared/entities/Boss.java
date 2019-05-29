package shared.entities;

import java.util.ArrayList;
import java.util.List;

import client.view.RenderItem;
<<<<<<< HEAD
import shared.general.Level;
=======
import shared.tiles.Tile;
>>>>>>> 9c7e6de2c54021304a64682e58adaabe43dc2f62

/**
 * class that handles actions and rendering of the boss mob
 * @author Jelmer Firet
 */
public class Boss extends Entity {
<<<<<<< HEAD
	
=======
	private double xPos,yPos;

>>>>>>> 9c7e6de2c54021304a64682e58adaabe43dc2f62
	/**
	 * Initialises the boss mob
	 * @param x the x position of the feet of the Boss (game pixels)
	 * @param y the y position of the feet of the Boss (game pixels)
	 */
<<<<<<< HEAD
  Boss(double x, double y) {
    super(x, y);
  }
=======
	public Boss(double x, double y){
		this.xPos = x;
		this.yPos = y;
	}
>>>>>>> 9c7e6de2c54021304a64682e58adaabe43dc2f62

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
