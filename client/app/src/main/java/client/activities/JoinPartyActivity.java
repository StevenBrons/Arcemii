package client.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.debernardi.archemii.R;

import client.controller.ClientGameHandler;
import shared.messages.JoinPartyMessage;

public class JoinPartyActivity extends AppCompatActivity {

	private EditText gamePin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_party);

        gamePin = findViewById(R.id.gamePinField);
    }

    public void onStartParty(View v){

		if(gamePin.getText().toString().length() == 5) {
			ClientGameHandler.handler.setJoinPartyActivity(this);

			int partyId = Integer.parseInt(gamePin.getText().toString());
			ClientGameHandler.sendMessage(new JoinPartyMessage(partyId));
		}

        // if successful then lobby screen opens.
    }

    public void openLobby(){
		Intent intStartParty = new Intent(this, LobbyActivity.class);
		startActivity(intStartParty);
	}
}
