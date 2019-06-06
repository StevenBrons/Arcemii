package shared.abilities;

import shared.entities.Entity;
import shared.general.Level;

public class Move implements Ability{

	private double speed = 0.05;
	private double direction;

	public Move(double speed){
		this.speed = speed;
	}

	public Ability invoke(double direction) {
		this.direction = direction;
		return this;
	}

	@Override
	public boolean execute(Level level, Entity self) {
		double x = self.getX();
		double y = self.getY();

		double dx = Math.cos(direction) * speed;
		double dy = Math.sin(direction) * speed;
		if (level.freeLine(x,y,x+dx,y+dy)){
			self.setPos(x + dx,y + dy);
			self.setVel(dx,dy);
		}
		return true;
	}

	@Override
	public String getName(){
		return "Move";
	}

	@Override
	public String getDescription(){
		return "Walk around like a real badass.";
	}

	@Override
	public String toString() {
		return "Move(" + direction + ")";
	}

	@Override
	public boolean available(Level level, Entity self){
		double x = self.getX();
		double y = self.getY();

		double dx = Math.cos(direction) * speed;
		double dy = Math.sin(direction) * speed;
		return level.freeLine(x, y, x + dx, y + dy);
	}
}
