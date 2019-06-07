package shared.general;

import java.util.ArrayList;

import shared.entities.Boss;
import shared.entities.Entity;
import shared.entities.Player;
import shared.messages.Message;
import shared.tiles.Start;
import shared.tiles.Tile;
import shared.tiles.Void;

public class Level extends Message {

  private Tile[][] tiles;
  private ArrayList<Entity> entities;
  private ArrayList<Entity> newEntities;
  private transient ArrayList<Object> updates = new ArrayList<>();
  private double spawnX = 0,spawnY = 0;

  /**
   * Create a level class with the specified tiles and entities as its contents
   * @param tiles A 2D tile array with the specified tiles, tiles shouldn't be null
   * @param entities a (potentially empty) entity array
   * @author Steven Bronsveld
   */
  public Level (Tile[][] tiles, ArrayList<Entity> entities) {
    this.tiles = tiles;
    this.entities = entities;
    this.newEntities = new ArrayList<>();

    for (int x = 0; x < tiles.length; x++) {
      for (int y = 0; y < tiles[x].length; y++) {
        if (tiles[x][y] instanceof Start) {
          spawnX = x + 0.5;
          spawnY = y - 0.5;
        }
      }
    }
  }

  /**
   * @return the width of the level, based on the tiles
   */
  public int getWidth(){
    return tiles.length;
  }

  /**
   * @return the height of the level, based on the tiles
   */
  public int getHeight(){
    return tiles[0].length;
  }

  /**
   * @return the tile a this grid position, returns void if the grid position falls outside of the level bounds
   */
  public Tile getTileAt(int x, int y) {
    if (x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length) {
      return tiles[x][y];
    } else {
      return new Void();
    }
  }

  /**
   *
   * @return The number of entities in the level
   */
  public int getNumEntity(){
    return entities.size();
  }

  public Entity getEntityAt(int idx){
    return entities.get(idx);
  }


  /**
   * Execute actions of all entities
   */
  public void execute() {
    for (Entity e : entities) {
      e.executeAll(this);
    }
    entities.addAll(newEntities);
    newEntities.clear();
  }

  /**
   * Invoke abilities on all entities except for players, who invoke their own
   * @author Steven Bronsveld
   */
  public void invoke() {
    for (Entity e : entities) {
      if (!(e instanceof Player)) {
        e.invokeAll(this);
      }
    }
  }


  /**
   * Add entity to level
   * @param e Entity to add
   * @author Steven Bronsveld
   */
  public void addEntity(Entity e) {
    newEntities.add(e);
    e.setChanged(true);
  }

  /**
   * A string representation of the level, solid tiles are represented as "#", non solid tiles are represented as "."
   * @return The level string
   * @author Steven Bronsveld
   */
  @Override
  public String toString() {
    String tot = "";
    for (int y = tiles[0].length-1; y >= 0; y--) {
      for (int x = 0; x < tiles.length; x++) {
        tot += tiles[x][y].isSolid() ? "#" : ".";
      }
      tot += "\n";
    }
    return tot;
  }

  /**
   * Spawn a player in the level at the position of the Start Tile
   * @param player The player to spawn
   * @author Steven Bronsveld
   * @author Jelmer Firet
   */
  public synchronized void spawnPlayer(Player player) {
    player.setPos(spawnX,spawnY);
    entities.add(player);
  }

  /**
   * Returns changed entities, and removes all changes afterwarts.
   * @return All changed entities in the level since last time this function was called
   * @author Steven Bronsveld
   */
  public ArrayList<Entity> getChanges() {
    ArrayList<Entity> changedEntities = new ArrayList();
    for (Entity e : entities) {
      if (e.isChanged()) {
        changedEntities.add(e);
        e.setChanged(false);
      }
    }
    return changedEntities;
  }

  /**
   * Update entity, dead entities are removed
   * @param updateEntity The entity to update
   * @author Jelmer Firet
   */
  public void updateEntity(Entity updateEntity) {
    for (int i = entities.size() - 1; i >= 0; i--) {
      if (entities.get(i).equals(updateEntity)) {
        if (updateEntity.isDead()) {
          entities.remove(i);
        } else {
          entities.set(i,updateEntity);
        }
        return;
      }
    }
    entities.add(updateEntity);

  }

  public void removeDead(){
    for (int i = entities.size()-1;i>=0;i--){
      if (entities.get(i).isDead() && !(entities.get(i) instanceof Player)){
        entities.remove(i);
      }
    }
  }

  public void respawnDeadPlayers(){
    for (int i = entities.size()-1;i>=0;i--){
      if (entities.get(i).isDead() && entities.get(i) instanceof Player){
        entities.get(i).setPos(spawnX,spawnY);
        entities.get(i).heal(10000);
        entities.get(i).setChanged(true);
      }
    }
  }


  /**
   * @param sx The start x position
   * @param sy The start y position
   * @param ex The end x position
   * @param ey The end y position
   * @return True when an entity can doMove in a straight line from start to end;
   * @author Jelmer Firet
   */
  public boolean freeLine(double sx, double sy, double ex, double ey){
    if (getTileAt((int)sx,(int)sy).isSolid()) return false;
    if (getTileAt((int)ex,(int)ey).isSolid()) return false;
    for (int x = (int)Math.min(sx,ex)+1;x <= (int)Math.max(sx,ex);x++){
      double frac = ((double)x-sx)/(ex-sx);
      int y = (int)(sy+frac*(ey-sy));
      if (getTileAt(x,y).isSolid()) return false;
    }
    for (int y = (int)Math.min(sy,ey)+1;y <= (int)Math.max(sy,ey);y++){
      double frac = ((double)y-sy)/(ey-sy);
      int x = (int)(sx+frac*(ex-sx));
      if (getTileAt(x,y).isSolid()) return false;
    }
    return true;
  }

	public boolean isFinished(){
		for(Entity entity : entities){
			if(entity instanceof Boss){
				return false;
			}
		}
		return true;
	}

}
