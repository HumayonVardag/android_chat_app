package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    public static String Tag = "SettingsActivity";

    protected void onCreate(Bundle savedInstancedState) {
        try {
            super.onCreate(savedInstancedState);
            setContentView(R.layout.settingslayout);
            Button btn_friend_list = (Button) findViewById(R.id.btn_header_friendlist);
            Button btn_chat = (Button) findViewById(R.id.btn_header_chat);

            Button btn_friend_request_list = (Button) findViewById(R.id.btn_friendrequest_list);
            Button btn_add_f = (Button) findViewById(R.id.btn_addfriend);
            Button btn_remove_f = (Button) findViewById(R.id.btn_removefriend);
            Button btn_del_acc = (Button) findViewById(R.id.btn_deleteaccount);
            Button btn_sign_out = (Button) findViewById(R.id.btn_signout);
            Button btn_showuserprofile = (Button) findViewById(R.id.btn_showuserprofile_settings);
            Button btn_help = (Button) findViewById(R.id.btn_help);

            btn_friend_request_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(SettingsActivity.this, FriendRequestList.class);
                    startActivity(i);
                }
            });


            btn_chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(SettingsActivity.this, chatlistActivity.class);
                    startActivity(i);
                }
            });
            btn_friend_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(SettingsActivity.this, FriendlistActivity.class);
                    startActivity(i);
                }
            });


            btn_add_f.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SettingsActivity.this, AddFriend.class);
                    startActivity(intent);
                }
            });

            btn_remove_f.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SettingsActivity.this, RemoveFriend.class);
                    intent.putExtra("EXIT", false);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });

            btn_del_acc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SettingsActivity.this,DeleteAccount.class);
                    Log.i(Tag,"request done");
                    startActivity(intent);

                }
            });

            btn_sign_out.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SettingsActivity.this,SignoutActivity.class);
                    startActivity(intent);
                }
            });
            btn_showuserprofile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SettingsActivity.this,showuserprofile.class);
                    startActivity(intent);
                }
            });
            btn_help.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SettingsActivity.this, HelpActivity.class);
                    startActivity(intent);
                }
            });

        } catch (Exception e) {
            Log.i("exception", "message is" + e);
        }


    }




}