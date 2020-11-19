package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class emailVerifyActivity extends AppCompatActivity {

    Button movetologin;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verifyemaillayout);

        movetologin = findViewById(R.id.btn_verifyemail);

        movetologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(emailVerifyActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
