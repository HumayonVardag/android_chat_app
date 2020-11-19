package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class RemoveFriend extends AppCompatActivity {
    private static String Tag = "Remove Friend";
    OkHttpClient client;
    Request request;
    URL url;
    private String name;

    Button requestremovefriend;
    TextView name_removefriend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.removefriendlayout);
        name_removefriend = findViewById(R.id.name_removefriend);
        Button btn_friend_list = (Button) findViewById(R.id.btn_header_friendlist);
        Button btn_settings = (Button) findViewById(R.id.btn_header_settings);
        Button btn_chat = (Button) findViewById(R.id.btn_header_chat);
        requestremovefriend = findViewById(R.id.request_removefriend);
        try {
            url = new URL("http://10.0.2.2:5000/mobile_removefriend");//special alias for your local host loop back against 127.0.0.1
            Log.i(Tag,"Url created");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        requestremovefriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(Tag,"in request function");
                name = name_removefriend.getText().toString();
                if(TextUtils.isEmpty(name)){
                    name_removefriend.setError("Please Insert Data");
                }
                else if(name.length()>=41){
                    name_removefriend.setError("Length is too long");
                }
                else{
                    client = new OkHttpClient();
                    SessionManagement session = new SessionManagement(RemoveFriend.this);
                    int userid = session.getSession();
                    RequestBody requestBody = new FormBody.Builder().add("friendemail", name)
                            .add("senderid",""+userid).build();
                    Log.i(Tag,"Request Body Created");
                    request = new Request.Builder().url(url).post(requestBody).build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            Log.i(Tag,"Error"+e.getMessage());
                            RemoveFriend.this.runOnUiThread(new Runnable() {
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
                                RemoveFriend.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject json = new JSONObject(s);
                                            if(json.getString("request").equals("done")){
                                                showMessage("Done","Friend Removed");
                                                Log.i(Tag,"friend: "+name +" removed");
                                            }
                                            else{
                                                showMessage("Error","Unknown Error Occurred");
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
            }
        });



        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RemoveFriend.this, chatlistActivity.class);
                intent.putExtra("EXIT", false);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        btn_friend_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RemoveFriend.this, FriendlistActivity.class);
                startActivity(intent);
            }
        });
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RemoveFriend.this, SettingsActivity.class);
                intent.putExtra("EXIT", false);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
