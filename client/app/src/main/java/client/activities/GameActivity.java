package client.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
        double range = 0.2;
        if (Math.pow(xPercent,2) + Math.pow(yPercent,2) < Math.pow(range,2)) {
            double angle = Math.atan2(yPercent,xPercent);
            Log.d("ANGLE",angle + "");
            ClientGameHandler.handler.getPlayer().invokeMove(angle);
        }

    }
}
