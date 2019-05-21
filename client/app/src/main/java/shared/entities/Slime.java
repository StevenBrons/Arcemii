package shared.entities;

import shared.entities.Entity;
import client.view.RenderItem;
import shared.tiles.Tile;

public class Slime extends Entity {
	@Override
	public RenderItem getRenderItem(){
		return new RenderItem("testSlime",0, Tile.HEIGHT,0.0,1.0);
	}
}
