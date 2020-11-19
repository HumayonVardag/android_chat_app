package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class HelpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helpactivity);
        Button btn_friend_list = (Button) findViewById(R.id.btn_header_friendlist);
        Button btn_chat = (Button) findViewById(R.id.btn_header_chat);
        Button btn_settings = (Button) findViewById(R.id.btn_header_settings);

        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HelpActivity.this,chatlistActivity.class);
                startActivity(i);
            }
        });
        btn_friend_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HelpActivity.this,FriendlistActivity.class);
                startActivity(i);
            }
        });
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HelpActivity.this,SettingsActivity.class);
                startActivity(i);
            }
        });
    }
}
