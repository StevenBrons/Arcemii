package shared.abilities;

import shared.entities.Entity;
import shared.general.Level;

public class Move extends Ability{

  private static final double SPEED = 0.5;
  double direction;

  public Ability invoke(double direction) {
    this.direction = direction;
    return this;
  }

  @Override
  public boolean execute(Level level, Entity self) {
    double x = self.getX();
    double y = self.getY();
    //TODO
    self.setPos(Math.cos(direction) * SPEED,Math.sin(direction) * SPEED);
    return true;
  }
}
