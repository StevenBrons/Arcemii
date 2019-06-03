package shared.abilities;

import shared.entities.Entity;
import shared.general.Level;

public class Heal extends Ability {

	private static final String name = "heal";

	@Override
	public boolean execute(Level level, Entity self) {
		// TODO: Implement heal ability.
		return false;
	}
<<<<<<< HEAD

	@Override
	public Ability invoke(double direction) {
		return null;
	}
=======
>>>>>>> 200098043a4a2c9e53827c4bcccf6008ec38279a
	@Override
	public String getName(){return name;}
}
