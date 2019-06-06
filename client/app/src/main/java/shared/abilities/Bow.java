package shared.abilities;

import shared.entities.Entity;
import shared.general.Level;

public class Bow implements Ability {

	private double direction;
	private boolean damagePlayer;
	private long cooldown = System.currentTimeMillis();

	public Ability invoke(double direction,boolean damagePlayer) {
		this.direction = direction;
		this.damagePlayer = damagePlayer;
		cooldown = System.currentTimeMillis()+200;
		return this;
	}

	@Override
	public boolean available(Level level,Entity self){
		return System.currentTimeMillis() > cooldown;
	}

	@Override
	public boolean execute(Level level, Entity self) {
		double dx = Math.cos(direction)*0.4;
		double dy = Math.sin(direction)*0.4;
		level.addEntity(new shared.entities.Arrow(self.getX()+dx,self.getY()+dy,dx,dy,damagePlayer));
		return true;
	}

	@Override
	public String getName(){
		return "Bow and Arrow";
	}

	@Override
	public String getDescription() {
		return "Shoot an arrow with your bow!";
	}

	@Override
	public String toString() {
		return "Range(" + direction + "," + damagePlayer + ")";
	}

	@Override
	public String getId(){return "range";}
}
