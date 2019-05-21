package client.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import shared.entities.Entity;
import shared.entities.Player;
import shared.entities.Slime;
import shared.general.Level;
import shared.tiles.Empty;
import shared.tiles.Tile;

public class GameView extends View {
    Bitmap screen;
    Canvas temporary;
    Rect src;
    Rect des;

    public void init(){
        screen = Bitmap.createBitmap(getWidth()/4,getHeight()/4,
                Bitmap.Config.ARGB_8888);
        temporary = new Canvas(screen);
        src = new Rect(0,0,getWidth()/4,getHeight()/4);
        des = new Rect(0,0,getWidth(),getHeight());
    }

    public GameView(Context context) {
        super(context);
        Tile grass = new Empty();
        Entity player = new Player(24,36);
        Entity slime = new Slime();
        for (int x = 0;x<12;x++){
            for (int y = 0;y<18;y++){
                drawObjects.add(grass.getRenderItem(Tile.WIDTH*x,Tile.HEIGHT*y));
            }
        }
        drawObjects.add(player.getRenderItem());
        drawObjects.add(slime.getRenderItem());
    }

    private List<RenderItem> drawObjects = new ArrayList<>();

    @Override
    public void onDraw(Canvas canvas){
        if (screen == null){
            this.init();
        }
        Collections.sort(drawObjects);
        canvas.drawColor(Color.BLACK);
        for (RenderItem object: drawObjects){
            object.renderTo(temporary,0,0);
        }
        canvas.drawBitmap(screen,src, des,new Paint());
    }

    public void updateLevel(Level level){
        drawObjects.clear();
        for (int x = 0;x<level.getWidth();x++){
            for (int y = 0;y<level.getHeight();y++){
                drawObjects.add(level.getTileAt(x,y).getRenderItem(24* Tile.WIDTH,
                        24*Tile.HEIGHT+Tile.TOPOFFSET));
            }
        }
        for (int idx = 0;idx<level.getNumEntity();idx++){
            drawObjects.add(level.getEntityAt(idx).getRenderItem());
        }
    }
}
