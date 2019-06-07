package shared.tiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import client.view.RenderItem;

/**
 * Instances of this class represent the tile-grid in a level
 * @author Steven Bronsveld
 */
public abstract class Tile implements Serializable {


    public final static int WIDTH  = 24;
    public final static int HEIGHT = 24;
    public final static int TOPOFFSET = 24;
    public boolean isSolid() {
        return true;
    }

    /**
     * @param x x position of the topleft of a Tile (game pixels)
     * @param y y position of the topleft of a Tile (game pixels)
     * @return a fallback RenderItem for subclasses that haven't implemented this method
     * @author Jelmer Firet
     */
    public List<RenderItem> getRenderItem(int x, int y){
        List<RenderItem> result = new ArrayList<>();
        result.add(new RenderItem("fallback",
                Tile.WIDTH*x,-Tile.HEIGHT*y,0.0,1.0,0));
        return result;
    }
}
