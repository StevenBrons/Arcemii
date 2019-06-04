package shared.general;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

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

  public Level (Tile[][] tiles, ArrayList<Entity> entities) {
    this.tiles = tiles;
    this.entities = entities;
    this.newEntities = new ArrayList<>();
  }

  public int getWidth(){
    return tiles.length;
  }

  public int getHeight(){
    return tiles[0].length;
  }

  public Tile getTileAt(int x, int y) {
    if (x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length) {
      return tiles[x][y];
    } else {
      return new Void();
    }
  }

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
  }

  /**
   * Invoke abilities on all entities except for players, who invoke their own
   */
  public void invoke() {
    for (Entity e : entities) {
      if (!(e instanceof Player)) {
        e.invokeAll(this);
      }
    }
  }


  public void addEntity(Entity e) {
    newEntities.add(e);
    e.setChanged(true);
  }

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

  public synchronized void spawnPlayer(Player player) {
    double spawnX = 0;
    double spawnY = 0;

    for (int x = 0; x < tiles.length; x++) {
      for (int y = 0; y < tiles[x].length; y++) {
        if (tiles[x][y] instanceof Start) {
          spawnX = x + 0.5;
          spawnY = y - 0.5;
        }
      }
    }

    player.setPos(spawnX,spawnY);
    entities.add(player);
  }

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
      if (entities.get(i).isDead()){
        entities.remove(i);
      }
    }
  }


  /**
   * @param sx The start x position
   * @param sy The start y position
   * @param ex The end x position
   * @param ey The end y position
   * @return True when an entity can move in a straight line from start to end;
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
}
