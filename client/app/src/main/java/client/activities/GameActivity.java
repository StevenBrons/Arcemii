package client.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.debernardi.archemii.R;

import client.controller.ClientGameHandler;
import client.view.GameView;
import client.view.JoystickView;
import client.view.Texture;
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
