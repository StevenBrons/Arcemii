package client.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import client.view.GameView;
import client.view.Texture;
import shared.general.Level;

public class GameActivity extends AppCompatActivity {

    private final GameView view = new GameView(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Texture.init(getAssets());
        FrameLayout frame = new FrameLayout(this);
        frame.addView(view);
        setContentView(frame);
    }

    public void draw(Level level) {
        view.updateLevel(level);
        view.postInvalidate();
    }
}
