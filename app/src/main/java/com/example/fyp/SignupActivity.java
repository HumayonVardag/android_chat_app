package com.example.fyp;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import static android.view.View.*;

public class SignupActivity extends AppCompatActivity {
    databasehandler db;
    ProgressBar progressBar;

    private static String Tag = "check_signupactivity";

    private String email_signup,password_signup,name_signup,lastname_signup;
    private EditText et_name_signup,et_password_signup,et_email_signup,et_lastname_signup;

    URL url,urlguest;
    OkHttpClient client;
    Request request;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new databasehandler(this);
        setContentView(R.layout.signuplayout);

        et_name_signup = (EditText) findViewById(R.id.enter_name_signup);
        et_password_signup = (EditText) findViewById(R.id.enter_password_signup);
        et_email_signup = (EditText) findViewById(R.id.enter_email_signup);
        et_lastname_signup = (EditText) findViewById(R.id.enter_lastname_signup);
        progressBar = findViewById(R.id.progressBar_signup);
        progressBar.setVisibility(INVISIBLE);
        Button login_button = findViewById(R.id.btn_login_at_signup);
        Button signup_button = findViewById(R.id.btn_signup_at_signup);
        Button guest_signup = findViewById(R.id.signup_guestuser_btn);

        try {
            //url to which request is required
            url = new URL("http://10.0.2.2:5000/mobile_signup");//special alias for your local host loop back against 127.0.0.1
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            //url to which request is required
            urlguest = new URL("http://10.0.2.2:5000/mobile_guest_signup");//special alias for your local host loop back against 127.0.0.1
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }



        login_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }


        });

        signup_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(VISIBLE);

                email_signup = et_email_signup.getText().toString();
                password_signup = et_password_signup.getText().toString();
                name_signup = et_name_signup.getText().toString();
                lastname_signup = et_lastname_signup.getText().toString();
                //empty check
                if (TextUtils.isEmpty(email_signup) || TextUtils.isEmpty(password_signup)
                        || TextUtils.isEmpty(name_signup) || TextUtils.isEmpty(lastname_signup)
                        || !isValidEmail(email_signup)) {

                        if(!isValidEmail(email_signup)) { et_email_signup.setError("Email Not Valid"); }

                        else{
                            showMessage("Missing Data","Please Insert Data");
                        }

                        progressBar.setVisibility(INVISIBLE);
                }
                else if(name_signup.length()>=16 || lastname_signup.length()>=16 || email_signup.length()>=41 || password_signup.length()>=21){
                    if(name_signup.length()>=16)
                        et_name_signup.setError("Length is too long");
                    if(lastname_signup.length()>=16)
                        et_lastname_signup.setError("Length is too long");
                    if(email_signup.length()>=31)
                        et_email_signup.setError("Length is too long");
                    if(password_signup.length()>=21)
                        et_password_signup.setError("Length is too long");
                    progressBar.setVisibility(INVISIBLE);
                }

                else {

                    client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder().add("name",""+name_signup)
                            .add("lastname",""+lastname_signup).add("email",""+email_signup)
                            .add("password",""+password_signup).build();
                    Log.i(Tag,"Request Body Created");
                    request = new Request.Builder().url(url).post(requestBody).build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            Log.i(Tag,"Error"+e.getMessage());
                            progressBar.setVisibility(INVISIBLE);
                            SignupActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),"Connection not Established",Toast.LENGTH_LONG).show();
                                    showMessage("Error","Possible Connection Error");
                                }
                            });
                        }
                        @Override
                        public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                            progressBar.setVisibility(INVISIBLE);
                            if (response.isSuccessful()){
                                final String s = response.body().string();
                                SignupActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject json = new JSONObject(s);
                                            if(json.get("description").equals("CheckEmail")){
                                                showMessage("Sign Up","Account Created");
                                                Log.i(Tag,"account created");
                                                //User user = new User(json.getInt("userid"),json.getString("username"));
                                                //user.setTemp(false);
                                                //SessionManagement sessionManagement = new SessionManagement(SignupActivity.this);
                                                //sessionManagement.saveSession(user);
                                                Intent intent = new Intent(SignupActivity.this, emailVerifyActivity.class);
                                                startActivity(intent);
                                            }
                                            else{
                                                    if(json.get("description").equals("invalid email")){
                                                    et_email_signup.setError("Invalid Email");
                                                    Toast.makeText(getApplicationContext(),"email check",Toast.LENGTH_LONG).show();
                                                    Log.i(Tag,"invalid email");
                                                    }
                                                    else if(json.get("description").equals("invalid name")){
                                                    showMessage("Error","Invalid Name or Last Name");
                                                    et_name_signup.setError("Invalid Name");
                                                    et_lastname_signup.setError("Invalid Last Name");
                                                    Log.i(Tag,"invalid name or last name");
                                                    }
                                                    else if(json.get("description").equals("Existing email")){
                                                     showMessage("Error","Email Already Exist");
                                                     Log.i(Tag,"email already exist");
                                                      }
                                                    else if(json.get("description").equals("invalid password")){
                                                     et_password_signup.setError("Password Length is too Short");
                                                     Log.i(Tag,"password too short");
                                                     }
                                                    else{
                                                      showMessage("Error","Undefined Error");
                                                     Log.i(Tag,"undefined error");
                                                     }
                                                    Log.i(Tag, "in trybody else part");
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
        });



        guest_signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(VISIBLE);

                    client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder().add("","").build();
                    Log.i(Tag,"Request Body Created");
                    request = new Request.Builder().url(urlguest).post(requestBody).build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            Log.i(Tag,"Error"+e.getMessage());
                            progressBar.setVisibility(INVISIBLE);
                            SignupActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),"Connection not Established",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        @Override
                        public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                            progressBar.setVisibility(INVISIBLE);
                            if (response.isSuccessful()){
                                final String s = response.body().string();
                                SignupActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        try {
                                            JSONObject json = new JSONObject(s);
                                            if(json.get("status").equals("done")){
                                                Toast.makeText(SignupActivity.this, "Guest Account Created", Toast.LENGTH_SHORT).show();
                                                Log.i(Tag,"Guest account created");
                                                String i = json.getString("userid");
                                                User user = new User(json.getInt("userid"),json.getString("name"));
                                                user.setTemp(true);
                                                SessionManagement session =new SessionManagement(SignupActivity.this);
                                                session.saveSession(user);
                                                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                                startActivity(intent);
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

        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkSession();
    }
    private void checkSession() {
        //check if user is logged in
        //if user is logged in --> move to mainActivity

        SessionManagement sessionManagement = new SessionManagement(SignupActivity.this);
        int userID = sessionManagement.getSession();

        if(userID != -1){
            //user id logged in and so move to mainActivity
            moveToMainActivity();
        }
        else{
            //do nothing
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private void moveToMainActivity() {
        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
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
