package shared.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import client.view.RenderItem;
import shared.abilities.Spawn;
import shared.abilities.Teleport;
import shared.general.Level;
import shared.tiles.Tile;

/**
 * class that handles actions and rendering of the boss mob
 * @author Jelmer Firet
 */
public class Boss extends Entity {
	private Teleport teleport = new Teleport();
	private Spawn spawn = new Spawn();

	/**
	 * Initialises the boss mob
	 * @param x the x position of the feet of the Boss (game pixels)
	 * @param y the y position of the feet of the Boss (game pixels)
	 */
	public Boss(double x, double y){
		super(x,y);
		maxhealth = health = 100;
	}

  @Override
	public List<RenderItem> getRenderItem(){
		List<RenderItem> result = new ArrayList<>();
		result.add(new RenderItem("boss/bossFloating",
				(int)(Tile.WIDTH*xPos), -(int)(Tile.HEIGHT*yPos),0.5,1.0,2));
		return result;
	}

	@Override
	public void invokeAll(Level level) {
		Entity targetPlayer = null;
		for (int i = 0;i<level.getNumEntity();i++){
			if (level.getEntityAt(i) instanceof Player){
				Entity player = level.getEntityAt(i);
				if (level.freeLine(xPos,yPos,player.getX(),player.getY())){
					if (targetPlayer == null ||
							(this.getUUID().getLeastSignificantBits()^player.getUUID().getLeastSignificantBits()) <
							(this.getUUID().getLeastSignificantBits()^targetPlayer.getUUID().getLeastSignificantBits())
					){
						targetPlayer = player;
					}
				}
			}
		}
		if (targetPlayer == null) return;
		Random rand = new Random();
		if (teleport.available(level,this)){
			double direction = (rand.nextDouble()*2.0-1.0)*Math.PI;
			invoke(teleport.invoke(direction));
		}
		else if (spawn.available(level,this)){
			double direction = (rand.nextDouble()*2.0-1.0)*Math.PI;
			invoke(spawn.invoke(direction));
		}

	}

}
