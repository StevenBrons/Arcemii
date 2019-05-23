package shared.entities;

import java.util.ArrayList;
import java.util.List;

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
	public List<RenderItem> getRenderItem(){
		List<RenderItem> result = new ArrayList<>();
		RenderItem renderItem = new RenderItem("arrowHorizontal",xPos,yPos,0.5,0.5);
		renderItem.setRotation(180.0f/(float)Math.PI*(float)Math.atan2((float)yVel,(float) xVel));
		result.add(renderItem);
		return result;
	}
}
