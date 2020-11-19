package com.example.fyp;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
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

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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

public class chatlistActivity extends AppCompatActivity {
    String[] name;// = {"SHiekh", "Khan", "Umer", "Alpha"};
    int[] conversationid;
    ListView chatlist;

    ArrayList<String> nam = new ArrayList<String>();
    ArrayList<Integer> conv_id = new ArrayList<Integer>();

    URL url;
    OkHttpClient client;
    Request request;
    private static String Tag = "chatlistActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatlistlayout);

        Button btn_friend_list = (Button) findViewById(R.id.btn_header_friendlist);
        Button btn_settings = (Button) findViewById(R.id.btn_header_settings);
        client = new OkHttpClient();


        btn_friend_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(chatlistActivity.this, FriendlistActivity.class);
                startActivity(intent);
            }
        });
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(chatlistActivity.this, SettingsActivity.class);
                startActivity(intent);

            }
        });

        try {
            url = new URL("http://10.0.2.2:5000/mobile_chatlist");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        SessionManagement session = new SessionManagement(chatlistActivity.this);
        final int userid = session.getSession();
        RequestBody requestBody = new FormBody.Builder().add("userid",""+userid).build();
        request = new Request.Builder().url(url).post(requestBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(Tag,"Connection not established");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                   final String s = response.body().string();
                   chatlistActivity.this.runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           try {
                               JSONObject json = new JSONObject(s);
                               Log.i(Tag,"id: "+json.getString("id"));
                               JSONArray conversationid_list = json.getJSONArray("id");
                               Log.i(Tag,"other: "+json.getString("other"));
                               Log.i(Tag,"other_name: "+json.getString("other_name"));
                               JSONArray namelist = json.getJSONArray("other_name");
                               Log.i(Tag,"messege: "+json.getString("messege"));

                               for(int i=0;i<namelist.length();i++){
                                   Log.i(Tag,"simple get: "+namelist.getString(i));
                                   Log.i(Tag,"simple get: "+conversationid_list.getString(i));
                                   nam.add(namelist.getString(i));
                                   conv_id.add(conversationid_list.getInt(i));
                               }
                               Log.i(Tag,"after conversation id");
                               //converting list in array
                               name = new String[nam.size()];
                               name = nam.toArray(name);
                               Integer[] j = conv_id.toArray(new Integer[0]);
                               Log.i(Tag,"after conversation id");
                               conversationid = new int[conv_id.size()];
                               //conversationid = conv_id.toArray(conversationid);
                                int ii=0;
                               for(Integer n:j){
                                   conversationid[ii] = n;
                                   ii++;
                               }

                               //printing into log
                               for(int i = 0; i < nam.size() ; i++){
                                    Log.i(Tag,"conversation id: "+conversationid[i]);
                                   Log.d("string is",(String)name[i]);
                               }

                               chatlist = (ListView)findViewById(R.id.lv_chatlist);
                               Chatlistadapter ap = new Chatlistadapter();
                               chatlist.setAdapter(ap);
                               chatlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                   @Override
                                   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                       Intent i = new Intent(chatlistActivity.this, webviewActivity.class);
                                       i.putExtra("conv_id", conversationid[position]);
                                       Log.i(Tag,"conversation id in onclick: "+conversationid[position]);
                                       startActivity(i);
                                   }
                               });

                           } catch (JSONException e) {
                               e.printStackTrace();
                           }
                       }
                   });
                }
            }
        });




    }
    //test Adapter class for chat list
    public class Chatlistadapter extends BaseAdapter {
        Context context;
        public int getCount()
        {
            return name.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = getLayoutInflater().inflate(R.layout.chatmemberlayout, null);
            TextView nme = (TextView) convertView.findViewById(R.id.name_chatmember);

            nme.setText(name[position]);
            return convertView;

        }
    }

}
