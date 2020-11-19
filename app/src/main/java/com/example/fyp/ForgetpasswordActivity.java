package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

public class ForgetpasswordActivity extends AppCompatActivity {

    OkHttpClient client;
    RequestBody requestBody;
    Request request;
    URL url;

    EditText name,lastname,email;
    Button tosignup,sendemail;

    private static String Tag = "forgetpassword";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgetpasswordlayout);
        name = findViewById(R.id.enter_name_forgetpassword);
        lastname = findViewById(R.id.enter_lastname_forgetpassword);
        email = findViewById(R.id.enter_email_forgetpassword);
        tosignup = findViewById(R.id.btn_signup_at_forgetpassword);
        sendemail = findViewById(R.id.btn_forgetpassword_SendEmail);

        try {
            url = new URL("http://10.0.2.2:5000/mobile_forgetpassword");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }



        tosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgetpasswordActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });

        sendemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n = name.getText().toString();
                String l = lastname.getText().toString();
                String e = email.getText().toString();

                if(n.length()>=16 || l.length()>=16 || e.length()>=41){
                if(n.length()>=16)
                    name.setError("Length is too long");
                if(l.length()>=16)
                    lastname.setError("Length is too long");
                if(e.length()>=41)
                    email.setError("Length is too long");

                }

                else if(!(TextUtils.isEmpty(n)) && !(TextUtils.isEmpty(l)) && !(TextUtils.isEmpty(e))){
                    client = new OkHttpClient();
                    requestBody = new FormBody.Builder().add("name", ""+n)
                            .add("lastname",""+l)
                            .add("email",""+e).build();
                    request = new Request.Builder().url(url).post(requestBody).build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.i(Tag,"Connection not Established");
                            Toast.makeText(ForgetpasswordActivity.this, "Connection not Established", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if(response.isSuccessful()){
                                final String s = response.body().string();
                                ForgetpasswordActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject json = new JSONObject(s);
                                            if(json.getString("status").equals("done")){
                                                showMessage("Check Email","Email with Password is send");
                                            }
                                            else{
                                                showMessage("Error","No Account with Such Credentials");
                                            }
                                        } catch (JSONException ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                });
                            }


                        }
                    });

                }//end of empty check if

                else{
                    if(TextUtils.isEmpty(n)){
                        name.setError("Empty Field");
                    }
                    else if(TextUtils.isEmpty(l)){
                        lastname.setError("Empty Field");
                    }
                    else if(TextUtils.isEmpty(e)){
                        email.setError("Empty Field");
                    }
                }




            }
        });
    }
    public void showMessage(String Title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(Title);
        builder.setMessage(Message);
        builder.show();
    }

}
