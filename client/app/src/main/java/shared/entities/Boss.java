package shared.entities;

import client.view.RenderItem;

public class Boss extends Entity {
	private int xPos,yPos;

	public Boss(int x, int y){
		this.xPos = x;
		this.yPos = y;
	}

	@Override
	public RenderItem getRenderItem(){
		return new RenderItem("boss/bossFloating",xPos, yPos,0.5,1.0);
	}
}
