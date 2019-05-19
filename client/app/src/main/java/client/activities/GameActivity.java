package client.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.debernardi.archemii.R;

import client.view.GameView;
import client.view.Texture;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Texture.init(getAssets());
        FrameLayout frame = new FrameLayout(this);
        frame.addView(new GameView(this));
        setContentView(frame);
    }
}
