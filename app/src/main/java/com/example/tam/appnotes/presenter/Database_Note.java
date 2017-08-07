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
    static final String NOTES_TABLE = "Notes";
    static final String COLUMN_ID = "id_note";
    static final String COLUMN_TITLE = "title";
    static final String COLUMN_NOTE = "note";
    static final String COLUMN_ALARM = "alarm";
    static final String COLUMN_TIME_CURRENT = "time_current";
    static final String COLUMN_COLOR = "color";
    static final String COLUMN_IMAGE = "image";

    String NOTES_TABLE_CREATE = "CREATE TABLE "
            + NOTES_TABLE + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TITLE + " TEXT,"
            + COLUMN_NOTE + " TEXT,"
            + COLUMN_ALARM + " TEXT,"
            + COLUMN_TIME_CURRENT + " TEXT,"
            + COLUMN_COLOR + " INT,"
            + COLUMN_IMAGE + " BLOB"
            + ")";

    public Database_Note(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NOTES_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST "+ NOTES_TABLE);
        onCreate(db);

    }

   public void inSertNote(Note note){
       SQLiteDatabase database = this.getWritableDatabase();
       ContentValues values = new ContentValues();
       values.put(COLUMN_TITLE, note.title);
       values.put(COLUMN_NOTE, note.note);
       values.put(COLUMN_TIME_CURRENT, note.dateCurrent);
       values.put(COLUMN_ALARM, note.timer);
       values.put(COLUMN_COLOR, note.color);
       values.put(COLUMN_IMAGE, note.image);
       database.insert(NOTES_TABLE, null, values);
       database.close();
   }

    public Cursor getNoteId(int id) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery( "SELECT * FROM " + NOTES_TABLE + " WHERE " +
                COLUMN_ID + " =?", new String[] { Integer.toString(id) } );
        return cursor;
    }

    public Cursor GetNote(String sql) {
        SQLiteDatabase database = getWritableDatabase();
        return database.rawQuery(sql, null);
    }

    public Integer deleteNote(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(NOTES_TABLE, COLUMN_ID + " = ? ",
                new String[] { Integer.toString(id) });
    }

    public void updateNote(int id, Note note) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, note.title);
        values.put(COLUMN_NOTE, note.note);
        values.put(COLUMN_TIME_CURRENT, note.dateCurrent);
        values.put(COLUMN_ALARM, note.timer);
        values.put(COLUMN_COLOR, note.color);
        values.put(COLUMN_IMAGE, note.image);
        database.update(NOTES_TABLE, values, COLUMN_ID + " = " + id, null);
        database.close();
    }


}
