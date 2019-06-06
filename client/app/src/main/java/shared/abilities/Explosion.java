package shared.abilities;

import shared.entities.Entity;
import shared.entities.Slime;
import shared.general.Level;

public class Explosion implements Ability {
	private double direction;
	private static final double dist = 3.0;
	private static final double range = 2.5;
	private long cooldown = System.currentTimeMillis();

	public Explosion(){
		super();
	}

	public Ability invoke(double direction){
		this.direction = direction;
		return this;
	}

	@Override
	public String getName(){
		return "Explosion";
	}

	@Override
	public String getDescription(){
		return "Explode your enemies with a blast!";
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
		if (!level.freeLine(x,y,x+dx,y+dy))
			for (int i = 0;i<level.getNumEntity();i++){
				Entity entity = level.getEntityAt(i);
				if (level.freeLine(x+dx,y+dy,entity.getX(),entity.getY()) &&
				    Math.hypot(entity.getX()-x-dx,entity.getY()-y-dy) < range){
					entity.damage(5);
				}
			}
		cooldown = System.currentTimeMillis()+5000;
		return false;
	}
}
