package com.debernardi.archemii;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CreateRoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);
    }

    public void onStartRoom(View v){
        Intent intStartRoom = new Intent(this,LobbyActivity.class);
        startActivity(intStartRoom);
    }
}
