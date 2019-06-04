package shared.abilities;

import shared.entities.Entity;
import shared.general.Level;

public class Move extends Ability{

  private static final String name = "move";

  private static final double SPEED = 0.1;
  double direction;

  public Ability invoke(double direction) {
    this.direction = direction;
    return this;
  }

  @Override
  public boolean execute(Level level, Entity self) {
    double x = self.getX();
    double y = self.getY();

    double dx = Math.cos(direction) * SPEED;
    double dy = Math.sin(direction) * SPEED;
    if (level.freeLine(x,y,x+dx,y+dy)){
      self.setPos(x + dx,y + dy);
    }
    return true;
  }
  @Override
  public String getName(){return name;}

  @Override
  public String toString() {
    return "move(" + direction + ")";
  }

  public boolean isPossible(Level level, Entity self){
    double x = self.getX();
    double y = self.getY();

    double dx = Math.cos(direction) * SPEED;
    double dy = Math.sin(direction) * SPEED;
    return level.freeLine(x, y, x + dx, y + dy);
  }
}
