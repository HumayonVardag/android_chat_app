package com.example.fyp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class FriendlistActivity extends AppCompatActivity {
    String[] name;//={"Humayon","Ali","Zain"};
    String[] email;//={"Humayon@gmail.com","Ali@gmail.com","Zain@gmail.com"};


    ArrayList<String> nam = new ArrayList<String>();
    ArrayList<String> eml = new ArrayList<String>();


    URL url;
    OkHttpClient client;
    Request request;

    private static String Tag = "FriendlistActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendlistlayout);
        Button btn_friend_list = (Button) findViewById(R.id.btn_header_friendlist);
        Button btn_settings = (Button) findViewById(R.id.btn_header_settings);
        Button btn_chat = (Button) findViewById(R.id.btn_header_chat);

        try {
            url = new URL("http://10.0.2.2:5000/mobile_friendlist");
            Log.i(Tag,"URL created");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        SessionManagement session = new SessionManagement(FriendlistActivity.this);
        final int userid = session.getSession();
        client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("userid",""+userid).build();
        request = new Request.Builder().url(url).post(requestBody).build();
        Log.i(Tag,"Request Created");
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(Tag,"Error"+e.getMessage());
                FriendlistActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Connection not Established",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(Tag,"In Response");
                if (response.isSuccessful()) {
                    final String s = response.body().string();
                        FriendlistActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Log.i(Tag,"In Run");
                                    JSONObject json = new JSONObject(s);
                                    if(json.getString("status").equals("done")){
                                        Log.i(Tag,"In json get string status");
                                        JSONArray namelist = json.getJSONArray("listname");
                                        JSONArray emaillist = json.getJSONArray("listemail");
                                        //converting objects into lists
                                        for(int i=0;i<namelist.length();i++){
                                            Log.i(Tag,"simple get: "+namelist.getString(i));
                                            nam.add(namelist.getString(i));
                                            eml.add(emaillist.getString(i));
                                        }
                                        //converting list in array
                                        name = new String[nam.size()];
                                        name = nam.toArray(name);
                                        email = new String[eml.size()];
                                        email = eml.toArray(email);
                                        //printing into log
                                        for(int i = 0; i < nam.size() ; i++){

                                            Log.d("string is",(String)name[i]);
                                        }

                                        ListView lv_friends= (ListView)findViewById(R.id.lv_friendlist);
                                        FriendListAdapter adp = new FriendListAdapter();
                                        lv_friends.setAdapter(adp);
                                    }
                                    
                            }
                                catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });


                }
            }
        });

        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendlistActivity.this, chatlistActivity.class);
                startActivity(intent);
            }
        });

        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendlistActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });




    }


    public void showMessageremove(String Title, String Message,final String friendemail){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(Title);
        builder.setMessage(Message);
        builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    remove(friendemail);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
    public void showMessage(String Title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(Title);
        builder.setMessage(Message);
        builder.show();

    }

    protected void remove(String email){
        SessionManagement session = new SessionManagement(FriendlistActivity.this);
        final int userid = session.getSession();
        URL u = null;
        OkHttpClient client;
        client = new OkHttpClient();
        Request r;
        try {
         u = new URL("http://10.0.2.2:5000/mobile_removefriend");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = new FormBody.Builder().add("senderid",""+userid)
                .add("friendemail",""+email).build();
        
        r = new Request.Builder().url(u).post(requestBody).build();

        client.newCall(r).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(Tag,"Error"+e.getMessage());
                FriendlistActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Connection not Established",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    final String s = response.body().string();
                    FriendlistActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject json = new JSONObject(s);
                                if(json.getString("request").equals("done")){
                                    showMessage("Removed","Friend Removed");
                                    recreate();
                                    Log.i(Tag,"friend removed");
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

    public class FriendListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return name.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.friendlayout, null);
            TextView namee= (TextView) convertView.findViewById(R.id.name_friendlayout);
            TextView emaill= (TextView) convertView.findViewById(R.id.email_friendlayout);
            Button delete= (Button)convertView.findViewById(R.id.rmvfriend_friendlayout);


            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMessageremove("Remove Friend","Are you Sure",email[position]);
                }
            });

            namee.setText(name[position]);
            emaill.setText(email[position]);
            return convertView;
        }
    }

}
