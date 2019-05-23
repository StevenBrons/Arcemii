package shared.general;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

import shared.entities.Entity;
import shared.tiles.Tile;

public class Level implements Serializable {
    public static final long serialVersionUID = 1L;

    private Tile[][] tiles;
    private ArrayList<Entity> entities;
    private transient ArrayList<Object> updates = new ArrayList<>();

    Level (Tile[][] tiles, ArrayList<Entity> entities) {
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
            return tiles[x][y];
        }
    }

    public int getNumEntity(){
        return entities.size();
    }

    public Entity getEntityAt(int idx){
        return entities.get(idx);
    }

}
