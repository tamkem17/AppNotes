package com.example.tam.appnotes.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.tam.appnotes.R;
import com.example.tam.appnotes.model.Note;
import com.example.tam.appnotes.presenter.CommonActivity;
import com.example.tam.appnotes.presenter.Database_Note;

public class NewNoteActivity extends CommonActivity {
    private TextView mTxtAlarm;
    private EditText mEdtTitle, mEdtNote;
    private Spinner mSpnDate, mSpnTime;
    private Database_Note mDatabase;
    private ImageButton mImbtnCancel;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_previous_item);
        mTxtAlarm = (TextView) findViewById(R.id.txt_Alarm);
        mEdtNote = (EditText)findViewById(R.id.edt_note);
        mEdtTitle = (EditText)findViewById(R.id.edt_title);
        mImbtnCancel = (ImageButton)findViewById(R.id.imbt_Cancel);
        mSpnDate = (Spinner) findViewById(R.id.spn_Date);
        mSpnTime = (Spinner) findViewById(R.id.spn_Time);
        mDatabase = new Database_Note(this);
        mTxtAlarm.setOnClickListener(new alarmClick());
        mSpnDate.setOnItemSelectedListener(new ItemSelectedDate());
        mSpnTime.setOnItemSelectedListener(new ItemSelectedTime());
        mImbtnCancel.setOnClickListener(new canCelClick());
        getCurrentDate();
        showAlarm();
        mEdtTitle.addTextChangedListener(inputTextWatcher);
    }

    public class alarmClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            mSpnDate.setVisibility(View.VISIBLE);
            mSpnTime.setVisibility(View.VISIBLE);
            mTxtAlarm.setVisibility(View.GONE);
            mImbtnCancel.setVisibility(View.VISIBLE);
        }
    }

    public class canCelClick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            mSpnDate.setVisibility(View.GONE);
            mSpnTime.setVisibility(View.GONE);
            mTxtAlarm.setVisibility(View.VISIBLE);
            mImbtnCancel.setVisibility(View.GONE);
        }
    }

    public void insertNote() {
        String listPicturePath = mArrayPicture.toString();
        String pictuePath = listPicturePath.substring(1, listPicturePath.length() - 1);
        String alarm = mDate + mTime;
        mDatabase.inSertNote(new Note(
                mEdtTitle.getText().toString(),
                mEdtNote.getText().toString(),
                alarm.toString(),
                mTxtCurrentDate.getText().toString(),
                mNewColor,
                pictuePath
        ));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                startActivity(new Intent(NewNoteActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.item_accept:
                insertNote();
                startActivity(new Intent(NewNoteActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.item_grid:
                gridDialog(NewNoteActivity.this);
                break;
            case R.id.item_camera:
                cameraDialog(NewNoteActivity.this);
                break;
            default:
                break;
    }
        return super.onOptionsItemSelected(item);

    }

}
