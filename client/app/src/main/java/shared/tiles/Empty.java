package shared.tiles;

import java.util.ArrayList;
import java.util.List;

import client.view.RenderItem;

/**
 * Tile that has no special attributes
 * @author Jelmer Firet
 */
public class Empty extends Tile{
	/**
	 * @param x x position of the topleft of a Tile (game pixels)
	 * @param y y position of the topleft of a Tile (game pixels)
	 * @return the RenderItem for a grass tile
	 * @author Jelmer Firet
	 */
	public List<RenderItem> getRenderItem(int x, int y){
		List<RenderItem> result = new ArrayList<>();
		result.add(new RenderItem("tiles/grass",
				Tile.WIDTH*x,-Tile.HEIGHT*y,0.0,0.0,0));
		return result;
	}

	@Override
	public boolean isSolid() {
		return false;
	}
}
