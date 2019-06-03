package shared.abilities;

import shared.entities.Entity;
import shared.general.Level;

public class Melee extends Ability {

	@Override
	public boolean execute(Level level, Entity self) {
		// TODO: Implement melee ability.
		return false;
	}

	@Override
	public Ability invoke(double direction) {
		return null;
	}
}
