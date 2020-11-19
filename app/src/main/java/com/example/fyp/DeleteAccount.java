package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DeleteAccount extends AppCompatActivity {

    private static String Tag = "deleteAccount";
    OkHttpClient client;
    Request request;
    URL url;

    Button requestdeleteaccount,canceldeleterequest,btn_friend_list,btn_chat,btn_settings;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deleteaccount);
        requestdeleteaccount = findViewById(R.id.delete_deleteaccount);
        canceldeleterequest = findViewById(R.id.cancel_deleteaccount);
        btn_friend_list = (Button) findViewById(R.id.btn_header_friendlist);
        btn_settings = (Button) findViewById(R.id.btn_header_settings);
        btn_chat = (Button) findViewById(R.id.btn_header_chat);

        try {
            url = new URL("http://10.0.2.2:5000/mobile_deleteaccount");//special alias for your local host loop back against 127.0.0.1
            Log.i(Tag,"Url created");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        requestdeleteaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(Tag,"in request function");

                SessionManagement session = new SessionManagement(DeleteAccount.this);
                final int userid = session.getSession();

                client = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder().add("Request", "deleteAccount")
                        .add("senderid",""+userid).build();
                Log.i(Tag,"Request Body Created");
                request = new Request.Builder().url(url).post(requestBody).build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.i(Tag,"Error"+e.getMessage());
                        DeleteAccount.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),"Connection not Established",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                        if (response.isSuccessful()){
                            final String s = response.body().string();
                            DeleteAccount.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject json = new JSONObject(s);
                                        if(json.getString("request").equals("done")){

                                            showMessage("Done","Account Deleted");
                                            SessionManagement session = new SessionManagement(DeleteAccount.this);
                                            session.removeSession();
                                            Intent intent = new Intent(DeleteAccount.this,SignupActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                            Log.i(Tag,s);
                                            startActivity(intent);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                        }
                    }
                });
            }
        });

        canceldeleterequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeleteAccount.this,MainActivity.class);
                startActivity(intent);
            }
        });



        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeleteAccount.this,chatlistActivity.class);
                startActivity(intent);
            }
        });
        btn_friend_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeleteAccount.this,FriendlistActivity.class);
                startActivity(intent);
            }
        });
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeleteAccount.this,SettingsActivity.class);
                startActivity(intent);
            }
        });

    }
    public void showMessage(String Title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(Title);
        builder.setMessage(Message);
        builder.show();
    }

}
