package com.example.fyp;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class webviewActivity extends AppCompatActivity {
    private WebView view;
    String postData;
    String url;
    private static String Tag = "webview";
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webviewlayout);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        view = findViewById(R.id.webv);
        Button btn_friend_list = (Button) findViewById(R.id.btn_header_friendlist);
        Button btn_settings = (Button) findViewById(R.id.btn_header_settings);
        Button btn_chatlist = (Button) findViewById(R.id.btn_header_chat);
        btn_friend_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(webviewActivity.this, FriendlistActivity.class);
                startActivity(intent);
            }
        });
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(webviewActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
        btn_chatlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(webviewActivity.this, chatlistActivity.class);
                startActivity(intent);
            }
        });

        //view.setWebViewClient(new WebViewClient());
        view.setWebChromeClient(new WebChromeClient());
        Intent i = getIntent();
        int convid = i.getIntExtra("conv_id",0);
        Log.i(Tag,"convid: "+convid);
        SessionManagement session = new SessionManagement(webviewActivity.this);
        int userid = session.getSession();

        String url = "http://10.0.2.2:5000/mobile_chat";
        postData = "";
        try {
            postData = "id=" + URLEncoder.encode(""+userid, "UTF-8") + "&conv_id=" + URLEncoder.encode(""+convid,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        WebSettings settings = view.getSettings();
        settings.setJavaScriptEnabled(true);
        view.postUrl(url, postData.getBytes());
        view.postUrl(url, postData.getBytes());
        Window w = getWindow();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //reloadWebView();

    }
    public void reloadWebView(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.postUrl(url, postData.getBytes());
                //recreate();
            }
        }, 5000);}

    @Override
    public void onBackPressed() {

        if(view.canGoBack()){
            view.goBack();
        }
        else{
            super.onBackPressed();
        }
    }

}
