package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class splashscreenActivity extends AppCompatActivity {

    int SPLASH_TIME = 3000; //This is 3 seconds
    ProgressBar progressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spashscreenlayout);
        progressBar = findViewById(R.id.progressBar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do any action here. Now we are moving to next page
                Intent mySuperIntent = new Intent(splashscreenActivity.this, SignupActivity.class);
                startActivity(mySuperIntent);
                finish();

            }
        }, SPLASH_TIME);
    }
    //Method to run progress bar for 5 seconds


}
