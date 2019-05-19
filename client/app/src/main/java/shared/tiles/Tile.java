package shared.tiles;

import client.view.RenderItem;

public abstract class Tile {
    public final static int WIDTH  = 24;
    public final static int HEIGHT = 24;
    public final static int TOPOFFSET = 24;
    boolean isSolid() {
        return true;
    }
    public RenderItem getRenderItem(int x, int y){
        return new RenderItem("fallback",x,y,0.0,1.0);
    }
}
