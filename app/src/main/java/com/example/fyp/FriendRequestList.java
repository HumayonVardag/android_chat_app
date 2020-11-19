package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class FriendRequestList extends AppCompatActivity {
    String[] names;
    String[] emails;
    ArrayList<String> nam = new ArrayList<>();
    ArrayList<String> eml = new ArrayList<>();
    OkHttpClient client;
    Request request;
    URL url;
    private static String Tag = "FriendRequestListActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendrequestlist_layout);
        final ListView lv = findViewById(R.id.lv_friendrequest_list);
        Button btn_chat = findViewById(R.id.btn_header_chat);
        Button btn_settings = findViewById(R.id.btn_header_settings);
        Button btn_friendlist = findViewById(R.id.btn_header_friendlist);

        Log.i(Tag,"above Request Body");
        try {
            url = new URL("http://10.0.2.2:5000/mobile_friendrequestlist");Log.i(Tag,"link created");//special alias for your local host loop back against 127.0.0.1
        } catch (MalformedURLException e) { e.printStackTrace(); }

        SessionManagement session = new SessionManagement(FriendRequestList.this);
        int userid = session.getSession();

        client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("userid",""+userid).build();
        request = new Request.Builder().url(url).post(requestBody).build();
        Log.i(Tag,"Request Body Created ");
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i(Tag,"Error"+e.getMessage());
                FriendRequestList.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Connection not Established",Toast.LENGTH_LONG).show();
                    }
                });
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                if (response.isSuccessful()){
                    Log.i(Tag,"in run");
                    final String s = response.body().string();
                    FriendRequestList.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                Log.i(Tag,"in run");
                                JSONObject json = new JSONObject(s);
                                if(json.getString("confirmation").equals("true")){
                                    Log.i(Tag,"simple get: "+json.getJSONArray("listname"));
                                    Log.i(Tag,"simple getstring: "+json.getString("listname"));
                                    //getting listsobjects in return
                                    JSONArray namelist = json.getJSONArray("listname");
                                    JSONArray emaillist = json.getJSONArray("listemail");

                                    //converting objects into lists
                                    for(int i=0;i<namelist.length();i++){
                                        Log.i(Tag,"simple get: "+namelist.getString(i));
                                        nam.add(namelist.getString(i));
                                        eml.add(emaillist.getString(i));
                                    }
                                    //converting list in array
                                    names = new String[nam.size()];
                                    names = nam.toArray(names);
                                    emails = new String[eml.size()];
                                    emails = eml.toArray(emails);
                                    //printing into log
                                    for(int i = 0; i < nam.size() ; i++){

                                        Log.d("string is",(String)names[i]);
                                    }


                                    FriendrequestAdapter ad = new FriendrequestAdapter();
                                    lv.setAdapter(ad);

                                }


                                else{
                                    Toast.makeText(FriendRequestList.this, "User not found", Toast.LENGTH_SHORT).show();
                                }


                            } catch (JSONException e) {
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
                Intent intent = new Intent(FriendRequestList.this, chatlistActivity.class);
                startActivity(intent);
            }
        });

        btn_friendlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendRequestList.this, FriendlistActivity.class);
                startActivity(intent);
            }
        });

        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendRequestList.this, SettingsActivity.class);
                startActivity(intent);
            }
        });


    }

    private class FriendrequestAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return names.length;
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
            convertView = getLayoutInflater().inflate(R.layout.friendrequestlayout,null);
            TextView name = (TextView)convertView.findViewById(R.id.friendrequest_name);
            final TextView email = (TextView)convertView.findViewById(R.id.friendrequest_email);
            Button add = (Button)convertView.findViewById(R.id.btn_friendrequest_accept);
            Button reject = (Button)convertView.findViewById(R.id.btn_friendrequest_reject);

            name.setText(names[position]);
            email.setText(emails[position]);

            SessionManagement session = new SessionManagement(FriendRequestList.this);
            final int userid = session.getSession();

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(Tag, "position: "+position+" email: "+emails[position]);
                    requestaccepted(userid,emails[position]);
                }
            });

            reject.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Log.i(Tag, "position: "+position+" email: "+emails[position]);
                    requestrejected(userid,emails[position]);
                }
            });

            return convertView;
        }

    }

    private void initiateurl(){
        try {
            url = new URL("http://10.0.2.2:5000/mobile_respondtofriendrequest");
            //special alias for your local host loop back against 127.0.0.1
            Log.i(Tag,"link created");
        } catch (MalformedURLException e) { e.printStackTrace(); }
    }


    private void requestaccepted(int id,String email){
        try {
            url = new URL("http://10.0.2.2:5000/mobile_acceptfriendrequest");
            //special alias for your local host loop back against 127.0.0.1
            Log.i(Tag,"link created");
        } catch (MalformedURLException e) { e.printStackTrace(); }

        client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("userid",""+id)
                .add("email",""+email).build();
        request = new Request.Builder().url(url).post(requestBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(Tag,"Error"+e.getMessage());
                FriendRequestList.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Connection not Established",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(Tag,"In response");
                if(response.isSuccessful()){
                   final String s = response.body().string();
                    FriendRequestList.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject json = new JSONObject(s);
                                if(json.getString("status").equals("done")){
                                    Log.i(Tag,"Friend added");
                                    Toast.makeText(FriendRequestList.this, "Friend Added into Friendlist", Toast.LENGTH_SHORT).show();
                                    recreate();
                                }
                                else{
                                    Log.i(Tag,"error in Friend request acceptance");
                                    Toast.makeText(FriendRequestList.this, "Unknown Error Occoured", Toast.LENGTH_SHORT).show();
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


    private void requestrejected(int id,String email){
        try {
            url = new URL("http://10.0.2.2:5000/mobile_rejectfriendrequest");
            //special alias for your local host loop back against 127.0.0.1
            Log.i(Tag,"link created");
        } catch (MalformedURLException e) { e.printStackTrace(); }
        client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("userid",""+id)
                .add("email",""+email).build();
        request = new Request.Builder().url(url).post(requestBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(Tag,"Error"+e.getMessage());
                FriendRequestList.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Connection not Established",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    final String s = response.body().string();
                    FriendRequestList.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject json = new JSONObject(s);
                                if(json.get("status").equals("done")){
                                    Log.i(Tag,"Friend Request Rejected");
                                    Toast.makeText(FriendRequestList.this, "Friend Request Rejected", Toast.LENGTH_SHORT).show();
                                    recreate();
                                }
                                else{
                                    Toast.makeText(FriendRequestList.this, "Unknown Error Occoured", Toast.LENGTH_SHORT).show();
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
