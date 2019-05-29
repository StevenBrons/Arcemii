package client.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import client.controller.ClientGameHandler;
import shared.entities.Arrow;
import shared.entities.Boss;
import shared.entities.Entity;
import shared.entities.Player;
import shared.entities.Skeleton;
import shared.entities.Slime;
import shared.general.Level;
import shared.tiles.Empty;
import shared.tiles.Tile;
import shared.tiles.Wall;

/**
 * View that draws the game
 * @author Jelmer Firet
 */

public class GameView extends View {
    Bitmap screen;
    Canvas temporary;
    Rect src;
    Rect des;
    private List<RenderItem> renderItems = new ArrayList<>();
    private Level level;

    /**
     * Constructs objects used for drawing to prevent them from being constructed every tick
     * @author Jelmer Firet
     */
    public void init(){
        screen = Bitmap.createBitmap(getWidth()/4,getHeight()/4,
                Bitmap.Config.ARGB_8888);
        temporary = new Canvas(screen);
        src = new Rect(0,0,getWidth()/4,getHeight()/4);
        des = new Rect(0,0,getWidth(),getHeight());
    }

    /**
     * Construct a new GameView
     * TODO: Remove test drawing
     * @param context context to pass to View constructor
     * @author Jelmer Firet
     */
    public GameView(Context context) {
        super(context);
    }

    /**
     * Draws the level and all entities to the screen
     * @param canvas The Canvas to draw the level and entities on.
     */
    @Override
    public void onDraw(Canvas canvas){
        if (level == null) {
            Paint paint = new Paint();
            paint.setTextSize(80);
            canvas.drawText("Loading...",100,100,paint);
            return;
        }
        if (screen == null){
            this.init();
            updateLevel(level);
        }
        canvas.drawColor(Color.BLACK);
        Player player = ClientGameHandler.handler.getPlayer();
        int offsetX = (getWidth()/8-(int)(Tile.WIDTH*player.getxPos()));
        int offsetY = (getHeight()/8-(int)(Tile.HEIGHT*player.getyPos()));
        for (RenderItem object : renderItems){
            object.renderTo(temporary,offsetX,offsetY);
        }
        canvas.drawBitmap(screen,src, des,new Paint());
    }

    /**
     * Update the RenderItems to draw the new Level instead of an older one
     * @param level The level to draw
     */
    public void updateLevel(Level level){
        Log.d("level",level.toString() );
        this.level = level;
        renderItems.clear();
        Player player = ClientGameHandler.handler.getPlayer();
        renderItems.addAll(player.getRenderItem());
        for (int idx = 0;idx<level.getNumEntity();idx++){
            renderItems.addAll(level.getEntityAt(idx).getRenderItem());
        }

        int minX = -1+((int)(Tile.WIDTH*player.getxPos())-screen.getWidth()/2)/Tile.WIDTH;
        int maxX =  1+((int)(Tile.WIDTH*player.getxPos())+screen.getWidth()/2)/Tile.WIDTH;
        int minY = -1+((int)(Tile.HEIGHT*player.getyPos())-screen.getHeight()/2)/Tile.HEIGHT;
        int maxY =  1+((int)(Tile.HEIGHT*player.getyPos())+screen.getHeight()/2)/Tile.HEIGHT;
        for (int tileX = minX;tileX<=maxX;tileX++){
            for (int tileY = minY;tileY<=maxY;tileY++){
                renderItems.addAll(level.getTileAt(tileX,tileY)
                        .getRenderItem(tileX,tileY));
            }
        }
        Collections.sort(renderItems);
    }
}
