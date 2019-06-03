package shared.abilities;

import shared.entities.Entity;
import shared.general.Level;

public class Range extends Ability {

	private static final String name = "range";

	@Override
	public boolean execute(Level level, Entity self) {
		// TODO: Implement the range ability.
		return false;
	}
	@Override
	public String getName(){return name;}
}
