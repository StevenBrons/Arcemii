package shared.abilities;

import shared.entities.Entity;
import shared.general.Level;

public class Heal implements Ability {

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
	public String getName(){
		return "heal";
	}

	@Override
	public String getDescription(){
		return "Heal all of your friends!";
	}

	@Override
	public String getId(){return "Heal";}
}
