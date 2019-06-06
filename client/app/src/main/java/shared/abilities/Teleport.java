package shared.abilities;

import shared.entities.Entity;
import shared.general.Level;

public class Teleport extends Ability {
	private double direction;
	private static final double dist = 6.0;
	private long cooldown = System.currentTimeMillis();

	public Teleport(){
		super();
	}

	public Ability invoke(double direction){
		this.direction = direction;
		return this;
	}

	@Override
	public String getName() {
		return "teleport";
	}

	@Override
	public boolean available(Level level, Entity self){
		return cooldown < System.currentTimeMillis();
	}

	@Override
	public boolean execute(Level level, Entity self) {
		double x = self.getX();
		double y = self.getY();
		double dx = Math.cos(direction)*dist;
		double dy = Math.sin(direction)*dist;
		if (!level.getTileAt((int)(self.getX()+dx),(int)(self.getY()+dy)).isSolid())
			self.setPos(x+dx,y+dy);
		cooldown = System.currentTimeMillis()+3000;
		return false;
	}
}
