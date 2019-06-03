package shared.abilities;

import shared.entities.Entity;
import shared.general.Level;

public class Heal extends Ability {

	@Override
	public boolean execute(Level level, Entity self) {
		// TODO: Implement heal ability.
		return false;
	}

	@Override
	public Ability invoke(double direction) {
		return null;
	}
}
