package client.activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.debernardi.archemii.R;

import client.controller.ClientGameHandler;

public class SettingsActivity extends AppCompatActivity {

	private Button btnServermode;
	private TextView newUsername, currentUsername;

	/**
	 * Set the current username and the current servermode on the screen.
	 * @param savedInstanceState
	 * @author Bram Pulles
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		// Set the server button to the correct value.
		btnServermode = findViewById(R.id.btnServermode);
		SharedPreferences prefs = getSharedPreferences(getString(R.string.sharedpref_servermodeinfo), MODE_PRIVATE);
		String currentmode = prefs.getString(getString(R.string.sharedpref_servermode), getString(R.string.online));
		btnServermode.setText(currentmode);

		// Set the current username textview to the correct value.
		newUsername = findViewById(R.id.newUsername);
		currentUsername = findViewById(R.id.usernameTextview);
		SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sharedpref_playerinfo), MODE_PRIVATE);
		String username = sharedPreferences.getString(getString(R.string.sharedpref_username), "-");
		currentUsername.setText(username);

		//Set the sound button to the correct value
		Button btnSound = findViewById(R.id.btnSound);
		if(getSharedPreferences("audioprefs", MODE_PRIVATE).contains("muted")){
			btnSound.setText(R.string.audio_muted);
		}
		else{
			btnSound.setText(R.string.audio_unmuted);
		}
	}

	/**
	 * Set the button to the current server mode and switch servermode when the button is pressed.
	 * Also change the shared preferences accordingly.
	 * @param v
	 * @author Bram Pulles
	 */
	public void changeServermode(View v){
		SharedPreferences prefs = getSharedPreferences(getString(R.string.sharedpref_servermodeinfo), MODE_PRIVATE);
		String currentmode = prefs.getString(getString(R.string.sharedpref_servermode), getString(R.string.online));

		String nextmode;
		if(currentmode.equals(getString(R.string.online))){
			btnServermode.setText(getString(R.string.offline));
			nextmode = getString(R.string.offline);
		}else{
			btnServermode.setText(getString(R.string.online));
			nextmode = getString(R.string.online);
		}

		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(getString(R.string.sharedpref_servermode), nextmode);
		editor.apply();
	}

	/**
	 * Save the new settings in the shared prefs and send a message to the server.
	 * @param v
	 * @author Bram Pulles
	 */
	public void saveUsername(View v){
		if(newUsername.getText().length() > 0 && newUsername.getText().length() < 10){
			currentUsername.setText(newUsername.getText().toString());

			SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sharedpref_playerinfo), MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString(getString(R.string.sharedpref_username), newUsername.getText().toString());
			editor.apply();

			ClientGameHandler.handler.sendPlayerInfoMessage();
		}
	}

	/**
	 * Toggles between a muted and unmuted state and save this in the shared prefs
	 * @param view
	 * @author Thijs van Loenhout
	 */
    public void onClickMute(View view) {
		TextView v = findViewById(R.id.btnSound);
		SharedPreferences prefs = getSharedPreferences("audioprefs", MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		if(prefs.contains("muted")){
			editor.remove("muted");
			v.setText(R.string.audio_unmuted);
		} else {
			editor.putBoolean("muted", true);
			v.setText(R.string.audio_muted);
		}
		editor.apply();
	}

}
