package shared.abilities;

import shared.entities.Entity;
import shared.entities.Slime;
import shared.general.Level;

public class Spawn implements Ability {
	private double direction;
	private static final double dist = 3.0;
	private long cooldown = System.currentTimeMillis();

	public Spawn(){
		super();
	}

	public Ability invoke(double direction){
		this.direction = direction;
		return this;
	}

	@Override
	public String getName() {
		return "Spawn";
	}

	@Override
	public String getDescription(){
		return "Spawn your own minion army!";
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
		if (level.freeLine(x,y,x+dx,y+dy))
			level.addEntity(new Slime(x+dx,y+dy));;
		cooldown = System.currentTimeMillis()+10000;
		return false;
	}
}
