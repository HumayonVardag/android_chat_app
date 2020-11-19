package com.example.fyp;

import android.content.Intent;
import android.database.Cursor;
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

public class LoginActivity extends AppCompatActivity {
    EditText Email_signin, Password_signin;
    String email,password;
    OkHttpClient client;
    Request request;
    URL url;
    private static String Tag = "loginActivity";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginlayout);
        Email_signin = (EditText) findViewById(R.id.enter_email_signin);
        Password_signin = (EditText) findViewById(R.id.enter_password_signin);

        try {
            url = new URL("http://10.0.2.2:5000/mobile_login");//special alias for your local host loop back against 127.0.0.1
        } catch (MalformedURLException e) { e.printStackTrace(); }

        Button btn_login = (Button) findViewById(R.id.btn_login_signin);
        Button btn_forgetpassword = findViewById(R.id.btn_forgetpassword_signin);

        btn_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                    email = Email_signin.getText().toString();
                    password = Password_signin.getText().toString();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    if(TextUtils.isEmpty(email))
                        Email_signin.setError("Field is empty");
                    else
                        Password_signin.setError("Field is Empty");
                }
                else if(email.length()>=41 || password.length()>=16){
                    if(email.length()>=41)
                        Email_signin.setError("Length is too long");
                    if(password.length()>=16)
                        Password_signin.setError("Length is too long");
                }
                    else {

                    client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder().add("email", email)
                            .add("password",password).build();
                    Log.i(Tag,"Request Body Created");
                    request = new Request.Builder().url(url).post(requestBody).build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            Log.i(Tag,"Error"+e.getMessage());
                            LoginActivity.this.runOnUiThread(new Runnable() {
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
                                LoginActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject json = new JSONObject(s);
                                            if(json.getBoolean("confirmation")){

                                                User user = new User(json.getInt("userid"),json.getString("username"));
                                                user.setTemp(false);
                                                SessionManagement sessionManagement = new SessionManagement(LoginActivity.this);
                                                sessionManagement.saveSession(user);
                                                Log.i(Tag,"session set in login");
                                                showMessage("Welcome","Welcome "+json.getString("username"));
                                                moveToMainActivity();
                                            }
                                            else{
                                                if(json.getString("detail").equals("verify Email")){
                                                    Log.i(Tag,"verify email");
                                                    showMessage("Error","Verify Email");
                                                }
                                                else if(json.getString("detail").equals("No Account")){
                                                    Log.i(Tag,"wrong credentials");
                                                    showMessage("Error","Wrong credentials");
                                                }

                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                            }
                        }
                    });


                     /*For private database

                    */
                    //showMessage("Error", "Some Error Occurred");


                }
            }
        });
        Button btn_signup = findViewById(R.id.btn_signup_at_login);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
        btn_forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetpasswordActivity.class);
                startActivity(intent);
            }
        });

    }//end on create

    @Override
    protected void onStart() {
        super.onStart();
        checkSession();
    }

    private void checkSession() {
        //check if user is logged in
        //if user is logged in --> move to mainActivity

        SessionManagement sessionManagement = new SessionManagement(LoginActivity.this);
        int userID = sessionManagement.getSession();

        if(userID != -1){
            //user id logged in and so move to mainActivity
            moveToMainActivity();
        }
        else{
            //do nothing
        }
    }

    private void moveToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void showMessage(String Title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(Title);
        builder.setMessage(Message);
        builder.show();
    }


    }

