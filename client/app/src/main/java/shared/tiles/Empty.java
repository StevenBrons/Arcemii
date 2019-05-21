package shared.tiles;

import client.view.RenderItem;

public class Empty extends Tile{
	public RenderItem getRenderItem(int x, int y){
		return new RenderItem("grassPlaceholder",x,y,0.0,0.0);
	}
}
