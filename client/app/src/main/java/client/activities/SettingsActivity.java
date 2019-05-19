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

    private TextView newUsername, currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        newUsername = findViewById(R.id.newUsername);

        currentUsername = findViewById(R.id.usernameTextview);
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sharedpref_playerinfo), MODE_PRIVATE);
        String username = sharedPreferences.getString(getString(R.string.sharedpref_username), "-");
        currentUsername.setText(username);
    }

    public void save(View v){
        if(newUsername.getText().length() > 0 && newUsername.getText().length() < 15){
            currentUsername.setText(newUsername.getText().toString());

            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sharedpref_playerinfo), MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getString(R.string.sharedpref_username), newUsername.getText().toString());
            editor.apply();

            ClientGameHandler.handler.playerInfoMessage();
        }
    }

}
