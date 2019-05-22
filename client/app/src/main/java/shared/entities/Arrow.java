package shared.entities;

import client.view.RenderItem;

public class Arrow extends Entity{
	private int xPos,yPos;
	private int xVel,yVel;

	public Arrow(int x,int y, int dx, int dy){
		this.xPos = x;
		this.yPos = y;
		this.xVel = dx;
		this.yVel = dy;
	}

	@Override
	public RenderItem getRenderItem(){
		return new RenderItem("arrowHorizontal",xPos,yPos,0.5,0.5);
	}
}
