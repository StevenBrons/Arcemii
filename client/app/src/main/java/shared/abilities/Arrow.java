package shared.abilities;

import shared.entities.Entity;
import shared.general.Level;

public class Arrow implements Ability {

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

	@Override
	public boolean execute(Level level, Entity self) {
		double dx = Math.cos(direction)*0.4;
		double dy = Math.sin(direction)*0.4;
		level.addEntity(new shared.entities.Arrow(self.getX()+dx,self.getY()+dy,dx,dy,damagePlayer));
		cooldown = System.currentTimeMillis()+4000;
		return true;
	}

	@Override
	public String getName(){
		return "Bow and arrow";
	}

	@Override
	public String getDescription(){
		return "Shoot an arrow with your bow!";
	}
}
