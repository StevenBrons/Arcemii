package client.activities;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.debernardi.archemii.R;

import java.io.IOException;
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

	private ImageButton[] images = new ImageButton[3];
	private ArrayList<Ability> myAbilities;

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
		synchronized (me){
			myAbilities = me.getAbilities();
		}

		frame.addView(UI);
		setContentView(frame);

		images[0] = findViewById(R.id.AbilityButtonBottom);
		images[1] = findViewById(R.id.AbilityButtonMiddle);
		images[2] = findViewById(R.id.AbilityButtonUpper);

		for(int i = 0; i < images.length; i++){
			makeDrawable(i);
		}

		BitmapDrawable res = null;
		try {
			res = (BitmapDrawable) Drawable.createFromStream(getAssets().open("sprites/heart.png"), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		res.getPaint().setFilterBitmap(false);
		if(res!=null)
			((ImageView) findViewById(R.id.heartImage)).setImageDrawable(res);
		ClientGameHandler.handler.setGameActivity(this);
	}

	/**
	 * Draw the ability card on screen.
	 * @param i
	 * @author Bram Pulles
	 */
	public void makeDrawable(int i){
		String uri = "sprites/ability_cards/";

		String temp = uri + myAbilities.get(i+1).getId() + ".png";
		BitmapDrawable res = null;
		try {
			res = (BitmapDrawable) Drawable.createFromStream(getAssets().open(temp), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		res.getPaint().setFilterBitmap(false);
		images[i].setImageDrawable(res);
	}

	public void draw(Level level) {
		if (view != null) {
			view.updateLevel(level);
			view.postInvalidate();
		}
	}

	/**
	 * Handles clicks on this ability button, fading the button until it is available
	 * @author Robert Koprinkov
	 * */
	public void onAbilityBottom(View view){
		Player player = ClientGameHandler.handler.getPlayer();
		synchronized (player){
			findViewById(R.id.AbilityButtonBottom).setAlpha((float) 0.6);
			player.invokeBottom();
			new Thread(() -> {
				try {
					Thread.sleep(player.getAbilities().get(1).getTimeout());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				findViewById(R.id.AbilityButtonBottom).setAlpha(1);
			}).start();
		}
	}

	/**
	 * Handles clicks on this ability button, fading the button until it is available
	 * @author Robert Koprinkov
	 * */
	public void onAbilityMiddle(View view){
		Player player = ClientGameHandler.handler.getPlayer();

		synchronized (player){
			findViewById(R.id.AbilityButtonMiddle).setAlpha((float) 0.6);
			player.invokeMiddle();
			new Thread(() -> {
				try {
					Thread.sleep(player.getAbilities().get(2).getTimeout());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				findViewById(R.id.AbilityButtonMiddle).setAlpha(1);
			}).start();
		}
	}

	/**
	 * Handles clicks on this ability button, fading the button until it is available
	 * @author Robert Koprinkov
	 * */
	public void onAbilityUpper(View view){
		Player player = ClientGameHandler.handler.getPlayer();
		synchronized (player){
			findViewById(R.id.AbilityButtonUpper).setAlpha((float) 0.6);
			player.invokeUpper();
			new Thread(() -> {
				try {
					Thread.sleep(player.getAbilities().get(3).getTimeout());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				findViewById(R.id.AbilityButtonUpper).setAlpha(1);
			}).start();
		}
	}


	public void updateHealth(int number){
		TextView tv1 = findViewById(R.id.textView3);
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
	 * Resume playing music
	 * @author Thijs van Loenhout
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

	/***
	 * When the audio track is completed, start it again.
	 * @author Thijs van Loenhout
	 */
	
	@Override
	public void onCompletion(MediaPlayer mp) {
		audio.start();
	}
}