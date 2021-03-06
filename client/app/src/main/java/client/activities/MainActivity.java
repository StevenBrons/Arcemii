package client.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.debernardi.archemii.R;

import client.controller.ClientGameHandler;
import shared.messages.CreatePartyMessage;

public class MainActivity extends AppCompatActivity {

	private SharedPreferences.OnSharedPreferenceChangeListener listener;
	private TextView connectionInfo;

	/**
	 * Uses the shared preferences to determine if the client is connected to the server.
	 * @return if the client is connected to the server.
	 * @author Bram Pulles
	 */
	private boolean isConnected(){
		SharedPreferences prefs = getSharedPreferences(getString(R.string.sharedpref_connectioninfo), MODE_PRIVATE);
		return prefs.getBoolean(getString(R.string.sharedpref_connection), false);
	}

	/**
	 * When the connection changes the listener will be called and change the status text on screen.
	 * @author Bram Pulles
	 */
	private void connectionListener(){
		SharedPreferences prefs = getSharedPreferences(getString(R.string.sharedpref_connectioninfo), MODE_PRIVATE);

		// Initialize to disconnected.
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(getString(R.string.sharedpref_connection), false);
		editor.apply();

		// Set up a listener for connection information.
		listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
			public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
				boolean connected = prefs.getBoolean(getString(R.string.sharedpref_connection), false);

				if(connected)
					connectionInfo.setText(R.string.connected);
				else
					connectionInfo.setText(R.string.disconnected);
			}
		};

		prefs.registerOnSharedPreferenceChangeListener(listener);
	}

	/**
	 * Initialize the client game handler.
	 * @param savedInstanceState
	 * @author Bram Pulles
	 * @author Steven Bronsveld
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		connectionInfo = findViewById(R.id.connectioninfo);
		connectionListener();

		ClientGameHandler.init(this);
	}

	/**
	 * If the device is connected to the server then create a new party.
	 * @param v
	 * @author Bram Pulles and Jelmer Firet
	 */
	public void onCreateParty(View v){
		if(isConnected()) {
			ClientGameHandler.sendMessage(new CreatePartyMessage());
			Intent intCreateParty = new Intent(this, LobbyActivity.class);
			startActivity(intCreateParty);
		}
	}

	/**
	 * If the device is connected to the server then go to the join party screen.
	 * @param v
	 * @author Bram Pulles and Jelmer Firet
	 */
	public void onJoinParty(View v){
		if(isConnected()){
			Intent intJoinParty = new Intent(this, JoinPartyActivity.class);
			startActivity(intJoinParty);
		}
	}

	public void onSettings(View v){
		Intent intSettings = new Intent(this, SettingsActivity.class);
		startActivity(intSettings);
	}
}
