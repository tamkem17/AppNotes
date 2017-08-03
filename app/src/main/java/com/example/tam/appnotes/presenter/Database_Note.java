package com.example.tam.appnotes.presenter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.example.tam.appnotes.model.Note;

/**
 * Created by tam on 8/3/2017.
 */

public class Database_Note extends SQLiteOpenHelper {
        static final String DATABASE_NAME = "DatabaseNote";
        static final int DATABASE_VERSION = 1;
        static final String DATABASE_CREATE_NOTE= "create table Notes(id_nhahang integer primary key autoincrement, "
                + " title text, node text, time text, datecurrent text, image blob);";
    public Database_Note(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_NOTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST Notes");
        onCreate(db);

    }
    public void InsertNote (String title, String note, String timer, String timeCurrent, byte[] image){
        SQLiteDatabase database = this.getWritableDatabase();
        String sql = "INSERT INTO Notes VALUES(null, ?, ?, ?, ?, ?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, title);
        statement.bindString(2, note);
        statement.bindString(3, timer);
        statement.bindString(4, timeCurrent);
        statement.bindBlob(5, image);
        statement.executeInsert();
    }

    public Cursor GetNote(String sql) {
        SQLiteDatabase database = getWritableDatabase();
        return database.rawQuery(sql, null);
    }


}
