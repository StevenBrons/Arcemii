package shared.tiles;

import java.util.ArrayList;
import java.util.List;

import client.view.RenderItem;

public abstract class Tile {
    public final static int WIDTH  = 24;
    public final static int HEIGHT = 24;
    public final static int TOPOFFSET = 24;
    boolean isSolid() {
        return true;
    }
    public List<RenderItem> getRenderItem(int x, int y){
        List<RenderItem> result = new ArrayList<>();
        result.add(new RenderItem("fallback",x,y,0.0,1.0));
        return result;
    }
}
