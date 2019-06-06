package client.activities;

import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.debernardi.archemii.R;

import java.util.ArrayList;

import client.controller.ClientGameHandler;
import client.view.GameView;
import client.view.JoystickView;
import client.view.Texture;
import shared.abilities.Ability;
import shared.entities.Player;
import shared.general.Level;

public class GameActivity extends AppCompatActivity implements JoystickView.JoystickListener, MediaPlayer.OnCompletionListener {

    private GameView view;
    private static MediaPlayer audio;
    private static boolean muted;

    /**
     * Makes a new GameView view in the activity and starts the refresh loop
     * @param savedInstanceState
     * @author Jelmer Firet
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        muted = getSharedPreferences("audioprefs", MODE_PRIVATE).contains("muted");
        view = new GameView(this);
        Texture.init(getAssets());
        FrameLayout frame = new FrameLayout(this);
        frame.addView(view);
        LayoutInflater factory = LayoutInflater.from(this);
        View UI = factory.inflate(R.layout.ui, null);
        Player me = ClientGameHandler.handler.getPlayer();
        ArrayList<Ability> myAbilities;
        synchronized (me){
            myAbilities = me.getAbilities();
        }
        frame.addView(UI);
        setContentView(frame);

        String uri = "@drawable/";

        final ImageButton ability1 = findViewById(R.id.AbilityButtonBottom);
        final ImageButton ability2 = findViewById(R.id.AbilityButtonMiddle);
        final ImageButton ability3 = findViewById(R.id.AbilityButtonUpper);

        String temp = uri + myAbilities.get(1).getId();
        int imageResource = getResources().getIdentifier(temp, null, getPackageName());
        Drawable res = getResources().getDrawable(imageResource);
        ability1.setImageDrawable(res);

        temp = uri + myAbilities.get(2).getId();
        imageResource = getResources().getIdentifier(temp, null, getPackageName());
        res = getResources().getDrawable(imageResource);
        ability2.setImageDrawable(res);

        temp = uri + myAbilities.get(3).getId();
        imageResource = getResources().getIdentifier(temp, null, getPackageName());
        res = getResources().getDrawable(imageResource);
        ability3.setImageDrawable(res);



        ClientGameHandler.handler.setGameActivity(this);
    }

    public void draw(Level level) {
        if (view != null) {
            view.updateLevel(level);
            view.postInvalidate();
        }
    }

    public void onAbilityBottom(View view){
        Player player = ClientGameHandler.handler.getPlayer();
        synchronized (player){
            player.invokeBottom();
        }
    }

    public void onAbilityMiddle(View view){
        Player player = ClientGameHandler.handler.getPlayer();
        synchronized (player){
            player.invokeMiddle();
        }
    }

    public void onAbilityUpper(View view){
        Player player = ClientGameHandler.handler.getPlayer();
        synchronized (player){
            player.invokeUpper();
        }
    }


    public void updateHealth(int number){
        TextView tv1 = (TextView) findViewById(R.id.textView3);
        String w = "" + number;
        tv1.setText(w);
    }

    @Override
    public void onJoystickMoved(float xPercent, float yPercent, int source) {
        Player player = ClientGameHandler.handler.getPlayer();
        double range = 0.2;
        double angle = -Math.atan2(yPercent,xPercent);
        if (Math.pow(xPercent,2) + Math.pow(yPercent,2) > Math.pow(range,2)) {
            synchronized (player){
                player.direction = angle;
                player.doMove = true;
            }
        } else {
            synchronized (player){
                player.doMove = false;
            }
        }

    }

    /**
    * handler to load and start music on start of activity
    * @author Thijs van Loenhout
    */
    @Override
    protected void onStart() {
        super.onStart();
        audio = MediaPlayer.create(this,R.raw.game);
        if(!muted)
            audio.start();
        audio.setLooping(true);
    }

    /**
     * handler to pause music when activity is paused
     * @author Thijs van Loenhout
     */
    @Override
    protected void onPause() {
        super.onPause();
        if(audio.isPlaying())
            audio.pause();
    }

    /**
     *
     *
     */
    @Override
    protected void onResume() {
        super.onResume();
        if(!muted)
                audio.start();
    }

    /**
     * handler to stop and release music when activity stops
     * @author Thijs van Loenhout
     */
    @Override
    protected void onStop() {
        super.onStop();
        audio.release();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        audio.start();
    }
}
