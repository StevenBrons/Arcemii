package shared.abilities;

import shared.entities.Arrow;
import shared.entities.Entity;
import shared.general.Level;

public class Range extends Ability {
	private double direction;
	private long cooldown = System.currentTimeMillis()+5000;

	public Ability invoke(double direction) {
		this.direction = direction;
		cooldown = System.currentTimeMillis()+5000;
		return this;
	}

	@Override
	public boolean available(Level level,Entity self){
		return System.currentTimeMillis() > cooldown;
	}

	private static final String name = "range";

	@Override
	public boolean execute(Level level, Entity self) {
		double dx = Math.cos(direction)*0.4;
		double dy = Math.sin(direction)*0.4;
		level.addEntity(new Arrow(self.getX()+dx,self.getY()+dy,dx,dy));
		return true;
	}

	@Override
	public String getName(){return name;}
}
