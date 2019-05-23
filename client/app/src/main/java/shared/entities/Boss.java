package shared.entities;

import java.util.ArrayList;
import java.util.List;

import client.view.RenderItem;

public class Boss extends Entity {
	private int xPos,yPos;

	public Boss(int x, int y){
		this.xPos = x;
		this.yPos = y;
	}

	@Override
	public List<RenderItem> getRenderItem(){
		List<RenderItem> result = new ArrayList<>();
		result.add(new RenderItem("boss/bossFloating",xPos, yPos,0.5,1.0));
		return result;
	}
}
