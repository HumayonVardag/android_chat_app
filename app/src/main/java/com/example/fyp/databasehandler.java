package com.example.fyp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class databasehandler extends SQLiteOpenHelper {
    public static final String Database_Name = "FYP";
    public static final String Table_Name = "registered_user";
    public static final String COL_Id = "ID";
    public static final String COL_Email = "EMAIL";
    public static final String COL_Password = "PASSWORD";

    public databasehandler(@Nullable Context context) {
        super(context, Database_Name, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    db.execSQL(" create table " + Table_Name + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,EMAIL TEXT,PASSWORD TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertData(String Email,String Password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_Email,Email);
        values.put(COL_Password,Password);

        Long result = db.insert(Table_Name, null, values);
        if(result==-1){
            return false;
        }
            return true;
    }

    public Cursor getData(){
        SQLiteDatabase db =this.getWritableDatabase();
        Cursor res =db.rawQuery("select * from "+Table_Name,null);

        return res;
    }
}
