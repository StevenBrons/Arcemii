package client.activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.debernardi.archemii.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        TextView currentUsername = findViewById(R.id.usernameTextview);
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sharedpref_filename), MODE_PRIVATE);
        String username = sharedPreferences.getString(getString(R.string.sharedpref_username), "-");
        currentUsername.setText(username);
    }
}
