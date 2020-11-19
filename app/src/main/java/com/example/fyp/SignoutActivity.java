package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.se.omapi.Session;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

public class SignoutActivity extends AppCompatActivity {
    URL url;
    public static String Tag = "signout";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SessionManagement session = new SessionManagement(SignoutActivity.this);
        Log.i(Tag,"out if");
        Boolean cond = session.getTemporary();
        //Toast.makeText(this, ""+cond, Toast.LENGTH_SHORT).show();
        if (cond){
            Log.i(Tag,"in if");
            final int userid = session.getSession();
            Log.i(Tag,"userid: "+userid);
            try {
                url = new URL("http://10.0.2.2:5000/mobile_deleteaccount");
                Log.i(Tag,"url created");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder().add("senderid", ""+userid)
                    .add("Request","delete guest user").build();
            Request r = new Request.Builder().url(url).post(requestBody).build();
            client.newCall(r).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Toast.makeText(SignoutActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String s = response.body().string();
                    Log.i(Tag,"in response");
                    SignoutActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject json = new JSONObject(s);
                                //Toast.makeText(SignoutActivity.this, "Guest Account Removed", Toast.LENGTH_SHORT).show();
                                Log.i(Tag,"Guest deleted");
                                session.removeSession();
                                Intent intent = new Intent(SignoutActivity.this,SignupActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        }
        else {
            session.removeSession();
            Intent intent = new Intent(SignoutActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
