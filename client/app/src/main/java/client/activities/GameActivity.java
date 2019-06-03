package client.activities;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        frame.addView(UI);
        setContentView(frame);

        final ImageButton ability1 = findViewById(R.id.AbilityButtonBottom);
        final ImageButton ability2 = findViewById(R.id.AbilityButtonMiddle);
        final ImageButton ability3 = findViewById(R.id.AbilityButtonUpper);
        /*
        Player me = ClientGameHandler.handler.getPlayer();
        ArrayList<Ability> myAbilities = me.getAbilities();



        String uri = "@drawable/";

        String temp = uri + myAbilities.get(1).getName();
        int imageResource = getResources().getIdentifier(temp, null, getPackageName());
        Drawable res = getResources().getDrawable(imageResource);
        ability1.setImageDrawable(res);

        temp = uri + myAbilities.get(2).getName();
        imageResource = getResources().getIdentifier(temp, null, getPackageName());
        res = getResources().getDrawable(imageResource);
        ability2.setImageDrawable(res);

        temp = uri + myAbilities.get(3).getName();
        imageResource = getResources().getIdentifier(temp, null, getPackageName());
        res = getResources().getDrawable(imageResource);
        ability3.setImageDrawable(res);

        */
        ability1.setImageResource(R.drawable.heal);
        ability2.setImageResource(R.drawable.melee);
        ability3.setImageResource(R.drawable.range);

        ClientGameHandler.handler.setGameActivity(this);
    }

    public void draw(Level level) {
        if (view != null) {
            view.updateLevel(level);
            view.postInvalidate();
        }
    }

    public void onAbilityBottom(View view){

    }

    public void onAbilityMiddle(View view){

    }

    public void onAbilityUpper(View view){

    }

    @Override
    public void onJoystickMoved(float xPercent, float yPercent, int source) {

    }
}
