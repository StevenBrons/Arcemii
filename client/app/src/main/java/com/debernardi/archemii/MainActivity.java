package com.debernardi.archemii;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onCreateRoom(View v){
        Intent intCreateRoom = new Intent(this,CreateRoomActivity.class);
        startActivity(intCreateRoom);
    }

    public void onJoinRoom(View v){
        Intent intJoinRoom = new Intent(this, JoinRoomActivity.class);
        startActivity(intJoinRoom);
    }

    public void onSettings(View v){
        Intent intSettings = new Intent(this,SettingsActivity.class);
        startActivity(intSettings);
    }
}
