package shared.abilities;

import shared.entities.Entity;
import shared.general.Level;

public class Heal extends Ability {

	private static final String name = "heal";
	private int healAmount = 0;

	@Override
	public boolean execute(Level level, Entity self) {
		self.heal(healAmount);
		return false;
	}

	public Ability invoke(int amount) {
		this.healAmount = amount;
		return this;
	}
	@Override
	public String getName(){return name;}
}
