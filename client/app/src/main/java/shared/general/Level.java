package shared.general;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

import shared.entities.Entity;
import shared.entities.Player;
import shared.messages.Message;
import shared.tiles.Tile;
import shared.tiles.Void;

public class Level extends Message {

  private Tile[][] tiles;
  private ArrayList<Entity> entities;
  private transient ArrayList<Object> updates = new ArrayList<>();

  public Level (Tile[][] tiles, ArrayList<Entity> entities) {
    this.tiles = tiles;
    this.entities = entities;
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

  @Override
  public String toString() {
    String tot = "";
    for (int x = 0; x < tiles.length; x++) {
      for (int y = 0; y < tiles[x].length; y++) {
        tot += tiles[x][y].isSolid() ? "#" : ".";
      }
      tot += "\n";
    }
    return tot;
  }
}
