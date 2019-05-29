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

import client.controller.ClientGameHandler;
import shared.entities.Arrow;
import shared.entities.Boss;
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
        ArrayList<Entity> entities = new ArrayList<>();
        entities.add(new Boss(36,96));
        entities.add(new Slime(84,72));
        entities.add(new Slime(108,72));
        ((Slime)entities.get(2)).setVelocity(-2,2);
        entities.add(new Skeleton(60,72));
        entities.add(new Skeleton(60,96));
        ((Skeleton)entities.get(4)).setVelocity(2,2);
        entities.add(new Skeleton(84,96));
        ((Skeleton)entities.get(5)).setShooting(true);
        ((Skeleton)entities.get(5)).setVelocity(-2,0);
        entities.add(new Player(60,48,1));
        entities.add(new Player(84,48,2));
        entities.add(new Player(108,48,3));
        entities.add(new Player(36,24,0));
        entities.add(new Player(60,24,1));
        entities.add(new Player(84,24,2));
        entities.add(new Player(108,24,3));
        ((Player)entities.get(9)).setVelocity(2,2);
        ((Player)entities.get(9)).setVelocity(-2,2);
        ((Player)entities.get(9)).setVelocity(2,2);
        ((Player)entities.get(9)).setVelocity(-2,2);
        entities.add(new Arrow(108,84,2,2));
        Tile[][] terrain = new Tile[12][18];
        for (int x = 0;x<12;x++){
            for (int y = 0;y<18;y++){
                if (x == 0 || x > 4 || y == 0 || y > 4){
                    terrain[x][y] = new Wall();
                }
                else{
                    terrain[x][y] = new Empty();
                }
            }
        }
        Level testLevel = new Level(terrain,entities);
        this.level = testLevel;
    }

    /**
     * Draws the level and all entities to the screen
     * @param canvas The Canvas to draw the level and entities on.
     */
    @Override
    public void onDraw(Canvas canvas){
        if (screen == null){
            this.init();
            updateLevel(level);
        }
        canvas.drawColor(Color.BLACK);
        Player player = ClientGameHandler.handler.getPlayer();
        int offsetX = getWidth()/8-player.getxPos();
        int offsetY = getHeight()/8-player.getyPos();
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
        this.level = level;
        renderItems.clear();
        Player player = ClientGameHandler.handler.getPlayer();
        renderItems.addAll(player.getRenderItem());
        for (int idx = 0;idx<level.getNumEntity();idx++){
            renderItems.addAll(level.getEntityAt(idx).getRenderItem());
        }

        int minX = -1+(player.getxPos()-screen.getWidth()/2)/Tile.WIDTH;
        int maxX =  1+(player.getxPos()+screen.getWidth()/2)/Tile.WIDTH;
        int minY = -1+(player.getyPos()-screen.getHeight()/2)/Tile.HEIGHT;
        int maxY =  1+(player.getyPos()+screen.getHeight()/2)/Tile.HEIGHT;
        for (int tileX = minX;tileX<=maxX;tileX++){
            for (int tileY = minY;tileY<=maxY;tileY++){
                renderItems.addAll(level.getTileAt(tileX,tileY)
                        .getRenderItem(Tile.WIDTH*tileX,Tile.HEIGHT*tileY));
            }
        }
        Collections.sort(renderItems);
    }
}
