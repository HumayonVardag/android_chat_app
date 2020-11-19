package com.example.fyp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SessionManagement {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY = "session_user";
    String temporary = "temp";

    private static String Tag = "session";

    public SessionManagement(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(User user){
        //save session of user whenever user is logged in
        int id = user.getId();
        Boolean temp = user.getTemp();
        Log.i(Tag,"in save session: "+temp);
        if(temp){
            editor.putBoolean(temporary,true);
        }
        else{
            editor.putBoolean(temporary,false);
        }
        editor.putInt(SESSION_KEY,id).commit();

    }

    public int getSession(){
        //return user id whose session is saved
        return sharedPreferences.getInt(SESSION_KEY, -1);
    }
    public Boolean getTemporary(){
        Boolean temp = sharedPreferences.getBoolean(temporary,false);
        Log.i(Tag,"in get session: "+temp);
        return temp;
                    }

    public void removeSession(){
        editor.putInt(SESSION_KEY,-1).commit();
    }
}
