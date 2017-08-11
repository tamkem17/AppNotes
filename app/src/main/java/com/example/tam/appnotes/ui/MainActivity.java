package com.example.tam.appnotes.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.tam.appnotes.R;
import com.example.tam.appnotes.model.Note;
import com.example.tam.appnotes.presenter.AlarmReceiver;
import com.example.tam.appnotes.presenter.CustomAdapterNote;
import com.example.tam.appnotes.presenter.Database_Note;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private GridView mGrvNotes;
    private CustomAdapterNote mAdapterNote = null;
    private ArrayList<Note> mArrayNote;
    private Database_Note mDatabase;
    private PendingIntent mPendingIntent;
    private AlarmManager mAlarmManager;
    private java.util.Calendar mCalender;
    private ArrayList<String> mAlarmTime = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
        LoadListNote();
        alarmService();
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
                    cursorNote.getInt(5),
                    cursorNote.getString(6)));
            mAlarmTime.add(cursorNote.getString(3));
            mAlarmTime.toString();
        }
        mAdapterNote = new CustomAdapterNote(this, R.layout.list_item_note, mArrayNote);
        mGrvNotes.setAdapter(mAdapterNote);
    }

    public void alarmService() {
        mCalender = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = null;
        if(mAlarmManager != null){
            try {
                for (int i =0; i< mAlarmTime.size(); i++) {
                    date = sdf.parse(mAlarmTime.get(i));
                }
                /*mCalender.setTimeInMillis(System.currentTimeMillis());
                mCalender.set(Calendar.HOUR_OF_DAY, date.getHours());
                mCalender.set(Calendar.MINUTE, date.getMinutes());
                mCalender.set(Calendar.DAY_OF_MONTH, date.getDate());
                mCalender.set(Calendar.MONTH, date.getMonth());
                mCalender.set(Calendar.YEAR, date.getYear());*/
                mAlarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
                mPendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
                mAlarmManager.set(AlarmManager.RTC_WAKEUP, mCalender.getTimeInMillis(), mPendingIntent);
                dialogAlarm();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void dialogAlarm() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Confirm Delete");
        alertDialogBuilder.setMessage("Are you sure you want to delete this");
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                mAlarmManager.cancel(mPendingIntent);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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
