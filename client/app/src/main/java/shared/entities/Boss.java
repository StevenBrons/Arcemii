package shared.entities;

import java.util.ArrayList;
import java.util.List;

import client.view.RenderItem;
import shared.general.Level;

/**
 * class that handles actions and rendering of the boss mob
 * @author Jelmer Firet
 */
public class Boss extends Entity {
	
	/**
	 * Initialises the boss mob
	 * @param x the x position of the feet of the Boss (game pixels)
	 * @param y the y position of the feet of the Boss (game pixels)
	 */
  Boss(double x, double y) {
    super(x, y);
  }

  @Override
	public List<RenderItem> getRenderItem(){
		List<RenderItem> result = new ArrayList<>();
		result.add(new RenderItem("boss/bossFloating",xPos, yPos,0.5,1.0));
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
