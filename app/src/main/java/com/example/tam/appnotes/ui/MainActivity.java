package com.example.tam.appnotes.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.example.tam.appnotes.R;
import com.example.tam.appnotes.model.Note;
import com.example.tam.appnotes.presenter.AlarmReceiver;
import com.example.tam.appnotes.presenter.CustomAdapterNote;
import com.example.tam.appnotes.presenter.Database_Note;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private GridView mGrvNotes;
    private CustomAdapterNote mAdapterNote = null;
    private ArrayList<Note> mArrayNote;
    private Database_Note mDatabase;
    private PendingIntent mPendingIntent;
    private AlarmManager mAlarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAlarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
        LoadListNote();
        mGrvNotes.setOnItemClickListener(new SeeDetailNote());
    }

    public class SeeDetailNote implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(MainActivity.this, DetailNoteActivity.class);
            intent.putExtra("idNote", mArrayNote.get(i).getIdNote());
            intent.putExtra("position", i);
            intent.putExtra("arrayNote", mArrayNote);
            startActivity(intent);
        }
    }

   public void LoadListNote() {
        mGrvNotes = (GridView) findViewById(R.id.grv_ListNotes);
        mDatabase = new Database_Note(getApplicationContext());
        mArrayNote = new ArrayList<Note>();
        Cursor cursorNote = mDatabase.GetNote("select * from Notes");
        while (cursorNote.moveToNext()) {
            mArrayNote.add(new Note(
                    cursorNote.getInt(0),
                    cursorNote.getString(1),
                    cursorNote.getString(2),
                    cursorNote.getString(3),
                    cursorNote.getString(4),
                    cursorNote.getString(5),
                    cursorNote.getInt(6),
                    cursorNote.getString(7)));
        }
        mAdapterNote = new CustomAdapterNote(this, R.layout.list_item_note, mArrayNote);
        mGrvNotes.setAdapter(mAdapterNote);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.item_new) {
            startActivity(new Intent(MainActivity.this, NewNoteActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
