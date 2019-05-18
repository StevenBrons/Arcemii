package client.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.debernardi.archemii.R;

import client.controller.ClientGameHandler;
import shared.messages.UpdatePartyMessage;

public class LobbyActivity extends AppCompatActivity {

	private TextView gamePin;

	/**
	 * This method also sets this activity available in the client game handler.
	 * @param savedInstanceState
	 * @author Jelmer Firet and Bram Pulles
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lobby);

		gamePin = findViewById(R.id.lobbyGamePin);
		ClientGameHandler.handler.setLobbyActivity(this);
		updatePartyMessage(ClientGameHandler.handler.getUpdatePartyMessage());
	}

	public void onGameSelect(View v){
		Intent intGameSelect = new Intent(this, GameSelectActivity.class);
		startActivity(intGameSelect);
	}

	public void onRandomGame(View v){
		Intent intGame = new Intent(this, GameActivity.class);
		startActivity(intGame);
	}

	/**
	 * Set the game pin and the players accordingly on the screen.
	 * @param m
	 * @author Bram Pulles
	 */
	public void updatePartyMessage(final UpdatePartyMessage m){
		// This is necessary because we are not invoking this method from the main thread.
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				gamePin.setText("Game PIN: " + m.getPartyId());
			}
		});

		//TODO: Show the players in the party.
	}
}
