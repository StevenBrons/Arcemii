package shared.abilities;

import shared.entities.Entity;
import shared.general.Level;

public class Melee extends Ability {

	private static final String name = "melee";

	@Override
	public boolean execute(Level level, Entity self) {
		// TODO: Implement melee ability.
		return false;
	}
	@Override
	public String getName(){return name;}
}
