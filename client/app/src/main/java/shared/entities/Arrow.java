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
		RenderItem renderItem = new RenderItem("arrowHorizontal",xPos,yPos,0.5,0.5);
		renderItem.setRotation(180.0f/(float)Math.PI*(float)Math.atan2((float)yVel,(float) xVel));
		return renderItem;
	}
}
