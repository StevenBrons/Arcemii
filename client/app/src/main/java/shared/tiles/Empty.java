package shared.tiles;

import java.util.ArrayList;
import java.util.List;

import client.view.RenderItem;

public class Empty extends Tile{
	public List<RenderItem> getRenderItem(int x, int y){
		List<RenderItem> result = new ArrayList<>();
		result.add(new RenderItem("grassPlaceholder",x,y,0.0,0.0));
		return result;
	}
}
