package shared.abilities;

import shared.entities.Entity;
import shared.entities.Player;
import shared.general.Level;

public class Melee implements Ability {

	private long cooldown = System.currentTimeMillis();
	private boolean attackPlayer = false;
	private int damage;

	@Override
	public boolean execute(Level level, Entity self) {
		for (int i = 0;i<level.getNumEntity();i++){
			Entity entity = level.getEntityAt(i);
			if (!entity.intersects(self) || entity == self)continue;
			if (entity instanceof Player && !attackPlayer)continue;
			if (!(entity instanceof Player) && attackPlayer)continue;
			entity.damage(damage);
		}
		return false;
	}

	public Ability invoke(boolean damagePlayer,int damage) {
		this.attackPlayer = damagePlayer;
		this.damage = damage;
		this.cooldown = System.currentTimeMillis()+1000;
		return this;
	}

	@Override
	public boolean available(Level level, Entity self){
		return (System.currentTimeMillis() > cooldown);
	}

	@Override
	public String getName(){
		return "melee";
	}

	@Override
	public String getDescription(){
		return "Slay your enemy with your bare hands.";
	}

	@Override
	public String getId(){return "Melee";}
}
