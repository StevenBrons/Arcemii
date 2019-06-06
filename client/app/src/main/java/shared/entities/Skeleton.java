package shared.entities;

import java.util.ArrayList;
import java.util.List;

import client.view.RenderItem;
import shared.abilities.Move;
import shared.abilities.Bow;
import shared.general.Level;
import shared.tiles.Tile;

import static java.lang.Math.atan2;

/**
 * Class that handles actions and rendering of a Skeleton
 * @author Jelmer Firet
 */
public class Skeleton extends Entity {

	private long shootingStart = System.currentTimeMillis()-5000;

	//server
	private transient Bow rangedAttack = new Bow();
	private transient Move move;

	/**
	 * Constructs a new skeleton
	 * @param x the x position of the feet of the skeleton (game pixels)
	 * @param y the y position of the feet of the skeleton (game pixels)
	 * @author Jelmer Firet
	 */
	public Skeleton(double x,double y){
		super(x,y);
		this.move = new Move(0.2);
		maxhealth = health = 5;
	}

	/**
	 * @return the RenderItem associated with the current state of this skeleton
	 * @author Jelmer Firet
	 */
	@Override
	public List<RenderItem> getRenderItem(){
		List<RenderItem> result = new ArrayList<>();
		RenderItem renderItem;
		if (shootingStart>System.currentTimeMillis()-2200){
			int offset = -(int)((shootingStart/200)%11);
			renderItem = new RenderItem("skeleton/skeletonShooting",
					(int)(Tile.WIDTH*xPos), -(int)(Tile.HEIGHT*yPos),0.5,1.0,2,offset);
		}
		else if (xVel*xVel+yVel*yVel>0.0001){
			renderItem = new RenderItem("skeleton/skeletonWalking",
					(int)(Tile.WIDTH*xPos), -(int)(Tile.HEIGHT*yPos),0.5,1.0,2);
		}
		else{
			renderItem = new RenderItem("skeleton/skeletonIdle",
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
		double averagePlayerX = 0.0;
		double averagePlayerY = 0.0;
		int numVisiblePlayer = 0;
		Entity targetPlayer = null;
		for (int i = 0;i<level.getNumEntity();i++){
			if (level.getEntityAt(i) instanceof Player){
				Entity player = level.getEntityAt(i);
				if (level.freeLine(xPos,yPos,player.getX(),player.getY())){
					if (Math.hypot(xPos-player.getX(),yPos-player.getY()) < 3.0){
						averagePlayerX += player.getX();
						averagePlayerY += player.getY();
						numVisiblePlayer++;
					}
					if (targetPlayer == null ||
						(this.getUUID().getLeastSignificantBits()^player.getUUID().getLeastSignificantBits()) <
						(this.getUUID().getLeastSignificantBits()^targetPlayer.getUUID().getLeastSignificantBits())
					){
						targetPlayer = player;
					}
				}
			}
		}
		if (numVisiblePlayer > 0){
			averagePlayerX /= numVisiblePlayer;
			averagePlayerY /= numVisiblePlayer;
			shootingStart = System.currentTimeMillis()-5000;
			invoke(this.move.invoke(atan2(yPos-averagePlayerY,xPos-averagePlayerX)));
			return;
		}
		if (rangedAttack.available(level,this) && targetPlayer != null
				&& shootingStart < System.currentTimeMillis()-3000){
			shootingStart = System.currentTimeMillis();
			setChanged(true);
		}
		if (rangedAttack.available(level,this) && targetPlayer != null
				&& shootingStart < System.currentTimeMillis()-1600 && shootingStart > System.currentTimeMillis()-1800){
			invoke(rangedAttack.invoke(atan2(targetPlayer.getY()-yPos,targetPlayer.getX()-xPos),true));
		}
	}


}
