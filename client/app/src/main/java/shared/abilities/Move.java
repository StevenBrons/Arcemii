package shared.abilities;

import shared.entities.Entity;
import shared.general.Level;

public class Move extends Ability{

  double direction;

  public Ability invoke(double direction) {
    this.direction = direction;
    return this;
  }

  @Override
  public boolean execute(Level level, Entity self) {
    return false;
  }
}
