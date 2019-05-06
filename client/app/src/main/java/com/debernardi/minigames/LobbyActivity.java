package com.debernardi.minigames;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LobbyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
    }

    public void onGameSelect(View v){
        Intent intGameSelect = new Intent(this,GameSelectActivity.class);
        startActivity(intGameSelect);
    }

    public void onRandomGame(View v){
        Intent intGame = new Intent(this,GameActivity.class);
        startActivity(intGame);
    }
}
