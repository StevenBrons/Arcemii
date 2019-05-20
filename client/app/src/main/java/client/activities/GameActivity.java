package client.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.debernardi.archemii.R;

import client.view.GameView;
import client.view.Texture;

public class GameActivity extends AppCompatActivity {
    private boolean running = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Texture.init(getAssets());
        FrameLayout frame = new FrameLayout(this);
        final GameView view = new GameView(this);
        frame.addView(view);
        setContentView(frame);
        this.running = true;
        Thread gameLoop = new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) {
                    view.postInvalidate();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        gameLoop.start();
    }
}
