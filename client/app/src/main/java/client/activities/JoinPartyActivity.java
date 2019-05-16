package client.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.debernardi.archemii.R;

public class JoinPartyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_party);
    }

    public void onStartParty(View v){
        Intent intStartParty = new Intent(this, LobbyActivity.class);
        startActivity(intStartParty);
    }
}
