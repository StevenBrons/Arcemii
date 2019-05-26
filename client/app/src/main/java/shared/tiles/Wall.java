package shared.tiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import client.view.RenderItem;

public class Wall extends Tile {
	private int randomX,randomY;

	public Wall(){
		Random r = new Random();
		randomX = r.nextInt(8)-4+Tile.WIDTH/2;
		randomY = r.nextInt(8)-4+Tile.HEIGHT/2;
	}

	@Override
	public List<RenderItem> getRenderItem(int x, int y){
		List<RenderItem> result = new ArrayList<>();
		result.add(new RenderItem("tree/treeBordered",x+randomX,y+randomY,0.5,40.0/48.0));
		result.add(new RenderItem("grassPlaceholder",x,y,0.0,0.0));
		return result;
	}
}
