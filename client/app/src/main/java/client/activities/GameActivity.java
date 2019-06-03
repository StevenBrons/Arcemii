package client.activities;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.debernardi.archemii.R;

import java.util.ArrayList;

import client.controller.ClientGameHandler;
import client.view.GameView;
import client.view.JoystickView;
import client.view.Texture;
import shared.abilities.Ability;
import shared.entities.Player;
import shared.general.Level;

public class GameActivity extends AppCompatActivity implements JoystickView.JoystickListener {

    private GameView view;

    /**
     * Makes a new GameView view in the activity and starts the refresh loop
     * @param savedInstanceState
     * @author Jelmer Firet
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = new GameView(this);
        Texture.init(getAssets());
        FrameLayout frame = new FrameLayout(this);
        frame.addView(view);
        LayoutInflater factory = LayoutInflater.from(this);
        View UI = factory.inflate(R.layout.ui, null);
        Player me = ClientGameHandler.handler.getPlayer();
        ArrayList<Ability> myAbilities = me.getAbilities();
        final ImageButton ability1 =(ImageButton) findViewById(R.id.AbilityButtonBottom);
        final ImageButton ability2 =(ImageButton) findViewById(R.id.AbilityButtonMiddle);
        final ImageButton ability3 =(ImageButton) findViewById(R.id.AbilityButtonUpper);
//        String uri = "@drawable/";
//        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
//        Drawable res = getResources().getDrawable(imageResource);
//        ability1.setImageDrawable(res);
        frame.addView(UI);
        setContentView(frame);
        ClientGameHandler.handler.setGameActivity(this);
    }

    public void draw(Level level) {
        if (view != null) {
            view.updateLevel(level);
            view.postInvalidate();
        }
    }

    public void onAbilityBottom(View view){
        ClientGameHandler.handler.getPlayer().invokeBottom();
    }

    public void onAbilityMiddle(View view){
        ClientGameHandler.handler.getPlayer().invokeMiddle();
    }

    public void onAbilityUpper(View view){
        ClientGameHandler.handler.getPlayer().invokeUpper();
    }

    @Override
    public void onJoystickMoved(float xPercent, float yPercent, int source) {
        Player player = ClientGameHandler.handler.getPlayer();
        double range = 0.2;
        double angle = Math.atan2(yPercent,xPercent);
        player.direction = angle;
        if (Math.pow(xPercent,2) + Math.pow(yPercent,2) > Math.pow(range,2)) {
            player.move = true;
        } else {
            player.move = false;
        }

    }
}
