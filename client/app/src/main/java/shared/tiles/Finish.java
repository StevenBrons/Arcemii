package shared.tiles;

import java.util.ArrayList;
import java.util.List;

import client.view.RenderItem;

public class Finish extends Tile {
	/**
	 * @param x x position of the topleft of a Tile (game pixels)
	 * @param y y position of the topleft of a Tile (game pixels)
	 * @return the RenderItem for a finish tile
	 * @author Jelmer Firet
	 */
	public List<RenderItem> getRenderItem(int x, int y){
		List<RenderItem> result = new ArrayList<>();
		result.add(new RenderItem("tiles/grass",
				Tile.WIDTH*x,-Tile.HEIGHT*y,0.0,0.0,0));
		result.add(new RenderItem("tiles/start",
				Tile.WIDTH*x+Tile.WIDTH/2,-Tile.HEIGHT*y+Tile.HEIGHT/2,0.5,0.5,1));
		return result;
	}

	@Override
	public boolean isSolid() {
		return false;
	}
}
