package client.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import client.controller.ClientGameHandler;
import client.view.GameView;
import client.view.Texture;
import shared.general.Level;

public class GameActivity extends AppCompatActivity {

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
        setContentView(frame);
        ClientGameHandler.handler.setGameActivity(this);
    }

    public void draw(Level level) {
        if (view != null) {
            view.updateLevel(level);
            view.postInvalidate();
        }
    }
}
