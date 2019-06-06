package shared.abilities;

import shared.entities.Arrow;
import shared.entities.Entity;
import shared.entities.Skeleton;
import shared.general.Level;

public class Range extends Ability {
	private double direction;
	private boolean damagePlayer;
	private long cooldown = System.currentTimeMillis();

	public Ability invoke(double direction,boolean damagePlayer) {
		this.direction = direction;
		this.damagePlayer = damagePlayer;
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
		level.addEntity(new Arrow(self.getX()+dx,self.getY()+dy,dx,dy,damagePlayer));
		cooldown = System.currentTimeMillis()+4000;
		return true;
	}

	@Override
	public String getName(){return name;}
}
