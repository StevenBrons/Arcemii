package shared.tiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import client.view.RenderItem;

/**
 * Tile that is unreachable by entities
 * @author Jelmer Firet
 */
public class Wall extends Tile {
	private int randomX,randomY;

	/**
	 * Constructor for a new Wall, sets random position for the root of the tree
	 * @author Jelmer Firet
	 */
	public Wall(){
		Random r = new Random();
		randomX = r.nextInt(8)-4+Tile.WIDTH/2;
		randomY = r.nextInt(8)-4+Tile.HEIGHT/2;
	}

	/**
	 * @param x x position of the topleft of a Tile (game pixels)
	 * @param y y position of the topleft of a Tile (game pixels)
	 * @return The 2 RenderItem's that represent the ground tile and a tree
	 * @author Jelmer Firet
	 */
	@Override
	public List<RenderItem> getRenderItem(int x, int y){
		List<RenderItem> result = new ArrayList<>();
		result.add(new RenderItem("tree/treeBordered",x+randomX,y+randomY,0.5,40.0/48.0));
		result.add(new RenderItem("grassPlaceholder",x,y,0.0,0.0));
		return result;
	}
}
