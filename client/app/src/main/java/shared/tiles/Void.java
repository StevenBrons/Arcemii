package shared.tiles;

import java.util.List;

import client.view.RenderItem;

public class Void extends Tile {
	@Override
	public List<RenderItem> getRenderItem(int x, int y){ return (new Wall()).getRenderItem(x,y);
	}
}
