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
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
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

public class showuserprofile extends AppCompatActivity {
    EditText name,lastname,email,password;
    Button btn_friend_list,btn_settings,btn_chat,btn_unlock,btn_update;
    OkHttpClient client;
    Request request;
    URL url,urlupdate;
    private static String Tag = "userprofile";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userprofile);

        name = findViewById(R.id.name_userprofile);
        lastname = findViewById(R.id.lastname_userprofile);
        email = findViewById(R.id.email_userprofile);
        password = findViewById(R.id.password_userprofile);
        btn_friend_list = (Button) findViewById(R.id.btn_header_friendlist);
        btn_settings = (Button) findViewById(R.id.btn_header_settings);
        btn_chat = (Button) findViewById(R.id.btn_header_chat);
        btn_unlock = findViewById(R.id.btn_unlockfield_userprofile);
        btn_update = findViewById(R.id.btn_update_userprofile);


        try {
            url = new URL("http://10.0.2.2:5000/mobile_showuserprofile");//special alias for your local host loop back against 127.0.0.1
        } catch (MalformedURLException e) { e.printStackTrace(); }

        try {
            urlupdate = new URL("http://10.0.2.2:5000/mobile_updateprofile");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        SessionManagement sessionManagement = new SessionManagement(showuserprofile.this);
        final int userid = sessionManagement.getSession();


        client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("userid",""+userid).build();
        Log.i(Tag,"Request Body Created");
        request = new Request.Builder().url(url).post(requestBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i(Tag,"Error"+e.getMessage());
                showuserprofile.this.runOnUiThread(new Runnable() {
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
                    showuserprofile.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject json = new JSONObject(s);
                                    String n = json.getString("name");
                                    String l = json.getString("lastname");
                                    String p = json.getString("password");
                                    name.setText(n);
                                    name.setEnabled(false);
                                    lastname.setText(l);
                                    lastname.setEnabled(false);
                                    email.setText(json.getString("email"));
                                    email.setEnabled(false);
                                    password.setText(p);
                                    password.setEnabled(false);
                                    btn_update.setEnabled(false);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
            }
        });

        btn_unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_update.setEnabled(true);
                name.setEnabled(true);
                lastname.setEnabled(true);
                password.setEnabled(true);
            }
        });
                btn_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String n = name.getText().toString();
                        String l = lastname.getText().toString();
                        String p = password.getText().toString();
                        if(TextUtils.isEmpty(n) || TextUtils.isEmpty(l) || TextUtils.isEmpty(p) || p.length()<3){
                            if(TextUtils.isEmpty(n))
                                name.setError("Empty Field");
                            else if(TextUtils.isEmpty(l))
                                lastname.setError("Empty Field");
                            else if(TextUtils.isEmpty(p))
                                password.setError("Empty Field");
                            else
                                password.setError("Length < 3");
                        }
                        else if(n.length()>=16 || l.length()>=16 || p.length()>=21){
                            if(n.length()>=16)
                                name.setError("Length is too long");
                            if(l.length()>=16)
                                lastname.setError("Length is too long");
                            if(p.length()>=21)
                                password.setError("Length is too long");
                        }
                        else{
                            RequestBody requestBody1 = new FormBody.Builder()
                                    .add("userid",""+userid)
                                    .add("name",""+n)
                                    .add("lastname",""+l)
                                    .add("password",""+p).build();

                            Request r = new Request.Builder().url(urlupdate).post(requestBody1).build();
                            client.newCall(r).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Log.i(Tag,"Connection Error");
                                    Toast.makeText(showuserprofile.this, "Connection Error", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    final String s = response.body().string();
                                    showuserprofile.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                JSONObject json = new JSONObject(s);
                                                if(json.getString("status").equals("done")){
                                                    Toast.makeText(showuserprofile.this, "Record Updated", Toast.LENGTH_SHORT).show();
                                                    name.setEnabled(false);
                                                    lastname.setEnabled(false);
                                                    password.setEnabled(false);
                                                    btn_update.setEnabled(false);
                                                }
                                                else{
                                                    Toast.makeText(showuserprofile.this, "UnKnown Error Occurred", Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    });

                                }
                            });
                        }

                    }
                });




        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(showuserprofile.this,chatlistActivity.class);
                startActivity(intent);
            }
        });
        btn_friend_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(showuserprofile.this,FriendlistActivity.class);
                startActivity(intent);
            }
        });
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(showuserprofile.this,SettingsActivity.class);
                startActivity(intent);
            }
        });

    }


}
