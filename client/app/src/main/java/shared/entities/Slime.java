package shared.entities;

import java.util.ArrayList;
import java.util.List;

import shared.abilities.Melee;
import shared.abilities.Move;
import shared.entities.Entity;
import client.view.RenderItem;
import shared.general.Level;
import shared.tiles.Tile;

/**
 * Class that handles actions and rendering of a slime
 * @author Jelmer Firet
 */
public class Slime extends Entity {

	Melee melee = new Melee();

	/**
	 * Constructs a new slime
	 * @param x x position of the bottom of the slime (game pixels)
	 * @param y y position of the bottom of the slime (game pixels)
	 * @author Jelmer Firet
	 */
	public Slime(double x,double y){
		super(x,y);
		this.move = new Move(0.03);
		maxhealth = health = 10;
	}


	/**
	 * @return the RenderItem associated with this slime
	 * @author Jelmer Firet
	 */
	@Override
	public List<RenderItem> getRenderItem(){
		List<RenderItem> result = new ArrayList<>();
		RenderItem renderItem;
		if (xVel*xVel+yVel*yVel > 0.0001){
			renderItem = new RenderItem("slime/redSlimeJump",
					(int)(Tile.WIDTH*xPos), -(int)(Tile.HEIGHT*yPos),0.5,1.0,2);
		}
		else{
			renderItem = new RenderItem("slime/testSlime",
					(int)(Tile.WIDTH*xPos), -(int)(Tile.HEIGHT*yPos),0.5,1.0,2);
		}
		if (xVel < 0){
			renderItem.setFlip(true);
		}
		result.add(renderItem);
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
		if (targetPlayer != null){
			invoke(this.move.invoke(Math.atan2(targetPlayer.getY()-yPos,targetPlayer.getX()-xPos)));
		}
		if (this.melee.available(level,this)){
			invoke(this.melee.invoke(true,5));
		}
	}

}
