package client.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.debernardi.archemii.R;

import client.controller.ClientGameHandler;
import shared.messages.JoinPartyMessage;

public class JoinPartyActivity extends AppCompatActivity {

	private EditText gamePin;

	/**
	 * This method also sets this activity available in the client game handler. Furthermore, the keyboard is shown.
	 * @param savedInstanceState
	 * @author Jelmer Firet, Robert Koprinkov and Bram Pulles
	 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_party);

        gamePin = findViewById(R.id.gamePinField);
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        ClientGameHandler.handler.setJoinPartyActivity(this);
    }

	/**
	 * If the game pin length filled in is 5 then this function sends a message to the server with the game pin.
	 * So the client game handler can open the lobby when the party was joined successfully.
	 * @param v
	 * @author Bram Pulles
	 */
	public void onStartParty(View v){
		if(gamePin.getText().length() > 0 && gamePin.getText().length() < 10) {
			int partyId = Integer.parseInt(gamePin.getText().toString());
			ClientGameHandler.sendMessage(new JoinPartyMessage(partyId));
		}
    }

	/**
	 * Open the lobby activity.
	 * @author Bram Pulles
	 */
	public void openLobby(){
		Intent intStartParty = new Intent(this, LobbyActivity.class);
		startActivity(intStartParty);
	}
}
