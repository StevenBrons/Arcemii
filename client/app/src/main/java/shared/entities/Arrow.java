package shared.entities;

import java.util.ArrayList;
import java.util.List;

import client.view.RenderItem;
import shared.general.Level;

public class Arrow extends Entity{

	Arrow(double x, double y) {
		super(x, y);
	}

	@Override
	public List<RenderItem> getRenderItem(){
		List<RenderItem> result = new ArrayList<>();
		RenderItem renderItem = new RenderItem("arrowHorizontal",xPos,yPos,0.5,0.5);
		renderItem.setRotation(180.0f/(float)Math.PI*(float)Math.atan2((float)yVel,(float) xVel));
		result.add(renderItem);
		return result;
	}

	@Override
	public void invokeAll(Level level) {
	}

	@Override
	public boolean update(Level level) {
		if (xVel != 0 && yVel != 0) {
			xPos += xVel;
			yVel += yVel;
		}
		// returns false even though a mutation is made!
		// this is because the client executes this in exactly the same manner!
		return false;
	}
}
