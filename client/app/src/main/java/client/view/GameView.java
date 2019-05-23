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
import shared.entities.Entity;
import shared.entities.Player;
import shared.entities.Skeleton;
import shared.entities.Slime;
import shared.general.Level;
import shared.tiles.Empty;
import shared.tiles.Tile;
import shared.tiles.Wall;

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
        Boss boss = new Boss(36,96);
        Slime slime = new Slime(84,72);
        Slime slime2 = new Slime(108,72);
        slime2.setVelocity(-2,2);
        Skeleton skeleton = new Skeleton(60,72);
        Skeleton skeleton2 = new Skeleton(60,96);
        skeleton2.setVelocity(2,2);
        Skeleton skeleton3 = new Skeleton(84,96);
        skeleton3.setShooting(true);
        skeleton3.setVelocity(-2,0);
        Player player2 = new Player(60,48,1);
        Player player3 = new Player(84,48,2);
        Player player4 = new Player(108,48,3);
        Player player5 = new Player(36,24,0);
        Player player6 = new Player(60,24,1);
        Player player7 = new Player(84,24,2);
        Player player8 = new Player(108,24,3);
        player5.setVelocity(2,2);
        player6.setVelocity(-2,2);
        player7.setVelocity(2,2);
        player8.setVelocity(-2,2);
        Arrow arrow = new Arrow(108,84,2,2);
        for (int x = 0;x<12;x++){
            for (int y = 0;y<18;y++){
                if (x == 0 || x > 4 || y == 0 || y > 4){
                    Wall tree = new Wall();
                    drawObjects.addAll(tree.getRenderItem(Tile.WIDTH*x,Tile.HEIGHT*y));
                }
                else{
                    Empty grass = new Empty();
                    drawObjects.addAll(grass.getRenderItem(Tile.WIDTH*x,Tile.HEIGHT*y));
                }
            }
        }
        drawObjects.addAll(ClientGameHandler.handler.getPlayer().getRenderItem());
        drawObjects.addAll(boss.getRenderItem());
        drawObjects.addAll(slime.getRenderItem());
        drawObjects.addAll(slime2.getRenderItem());
        drawObjects.addAll(skeleton.getRenderItem());
        drawObjects.addAll(skeleton2.getRenderItem());
        drawObjects.addAll(skeleton3.getRenderItem());
        drawObjects.addAll(player2.getRenderItem());
        drawObjects.addAll(player3.getRenderItem());
        drawObjects.addAll(player4.getRenderItem());
        drawObjects.addAll(player5.getRenderItem());
        drawObjects.addAll(player6.getRenderItem());
        drawObjects.addAll(player7.getRenderItem());
        drawObjects.addAll(player8.getRenderItem());
        drawObjects.addAll(arrow.getRenderItem());

    }

    private List<RenderItem> drawObjects = new ArrayList<>();

    @Override
    public void onDraw(Canvas canvas){
        if (screen == null){
            this.init();
        }
        Collections.sort(drawObjects);
        canvas.drawColor(Color.BLACK);
        Player player = ClientGameHandler.handler.getPlayer();
        int offsetX = getWidth()/8-player.getxPos();
        int offsetY = getHeight()/8-player.getyPos();
        for (RenderItem object: drawObjects){
            object.renderTo(temporary,offsetX,offsetY);
        }
        canvas.drawBitmap(screen,src, des,new Paint());
    }

    public void updateLevel(Level level){
        drawObjects.clear();
        for (int x = 0;x<level.getWidth();x++){
            for (int y = 0;y<level.getHeight();y++){
                drawObjects.addAll(level.getTileAt(x,y).getRenderItem(24* Tile.WIDTH,
                        24*Tile.HEIGHT+Tile.TOPOFFSET));
            }
        }
        for (int idx = 0;idx<level.getNumEntity();idx++){
            drawObjects.addAll(level.getEntityAt(idx).getRenderItem());
        }
    }
}
