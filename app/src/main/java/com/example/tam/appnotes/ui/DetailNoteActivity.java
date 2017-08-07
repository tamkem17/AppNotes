package com.example.tam.appnotes.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.util.Calendar;

import java.text.SimpleDateFormat;
import com.example.tam.appnotes.R;
import com.example.tam.appnotes.model.Note;
import com.example.tam.appnotes.presenter.CustomDialog;
import com.example.tam.appnotes.presenter.Database_Note;

public class DetailNoteActivity extends CustomDialog {
    private TextView mTxtAlarm;
    private Spinner mSpnDate, mSpnTime;
    private ImageButton mImbtnCancel;
    private int mIdNote;
    private EditText mEdtTitle, mEdtNote;
    private ImageView mImgNote;
    private Database_Note mDatabase;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_note);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_previous_item);
        mDatabase = new Database_Note(this);
        mTxtAlarm = (TextView)findViewById(R.id.txt_Alarm);
        mImbtnCancel = (ImageButton)findViewById(R.id.imbt_Cancel);
        mEdtNote = (EditText)findViewById(R.id.edt_note);
        mEdtTitle = (EditText)findViewById(R.id.edt_title);
        mImgNote = (ImageView)findViewById(R.id.img_newImage);
        mSpnDate = (Spinner) findViewById(R.id.spn_Date);
        mSpnTime = (Spinner) findViewById(R.id.spn_Time);
        mTxtAlarm.setOnClickListener(new alarmClick());
        mImbtnCancel.setOnClickListener(new canCelClick());
        mSpnDate.setOnItemSelectedListener(new ItemSelectedDate());
        mSpnTime.setOnItemSelectedListener(new ItemSelectedTime());
        Intent intent = getIntent();
        mIdNote = intent.getExtras().getInt("idNote");
        getCurrentDate();
        showAlarm();
        getNoteId();

    }

    public void getNoteId(){
        Cursor cursor = mDatabase.getNoteId(mIdNote);
        while (cursor.moveToNext()) {
            mEdtTitle.setText(cursor.getString(1));
            mEdtNote.setText(cursor.getString(2));
            byte[] imageByte = cursor.getBlob(6);
            ByteArrayInputStream imageStream = new ByteArrayInputStream(imageByte);
            Bitmap imageBitmap = BitmapFactory.decodeStream(imageStream);
            mImgNote.setImageBitmap(imageBitmap);
        }
    }

    public void updateNote(){
        try {
            mDatabase.updateNote(mIdNote, new Note(
                    mEdtTitle.getText().toString(),
                    mEdtNote.getText().toString(),
                    mDate.toString(),
                    mTxtCurrentDate.getText().toString(),
                    mNewColor,
                    ImageviewToBye(mImgNewImage)));
            Toast.makeText(getApplicationContext(), "Update successful", Toast.LENGTH_LONG).show();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Erro Update", Toast.LENGTH_LONG).show();

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

    public class alarmClick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            mSpnDate.setVisibility(View.VISIBLE);
            mSpnTime.setVisibility(View.VISIBLE);
            mTxtAlarm.setVisibility(View.GONE);
            mImbtnCancel.setVisibility(View.VISIBLE);
        }
    }

    public class ItemSelectedDate implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            SimpleDateFormat sdfomat = null;
            String strDateFormat = "dd/MM/yyyy";
            sdfomat = new SimpleDateFormat(strDateFormat);
            if(parent.getSelectedItemPosition() == 0) {
                String mToday = mTxtCurrentDate.getText().toString().substring(0, 10);
                Toast.makeText(getApplicationContext(), mToday, Toast.LENGTH_LONG).show();
            }
            if(parent.getSelectedItemPosition() == 1) {
                mCalendar.add(Calendar.DAY_OF_YEAR, 1);
                String mTomorrow = sdfomat.format(mCalendar.getTime());
                Toast.makeText(getApplicationContext(), mTomorrow, Toast.LENGTH_LONG).show();
                mCalendar = Calendar.getInstance();

            }
            if(parent.getSelectedItemPosition() == 2) {
                int weekday = mCalendar.get(Calendar.DAY_OF_WEEK);
                if (weekday!=Calendar.THURSDAY){
                    int days = (Calendar.SATURDAY - weekday + 5) % 7;
                    mCalendar.add(Calendar.DAY_OF_YEAR, days);
                    String mNextThursday = sdfomat.format(mCalendar.getTime());
                    Toast.makeText(getApplicationContext(), mNextThursday, Toast.LENGTH_LONG).show();
                    mCalendar = Calendar.getInstance();
                    return;
                }

            }
            else {
                datePickerDialog(DetailNoteActivity.this);
                Toast.makeText(getApplicationContext(), mDate, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    public class ItemSelectedTime implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (parent.getSelectedItemPosition() == 4) {
                timePickerDialog(DetailNoteActivity.this);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_detail_camera:
                cameraDialog(DetailNoteActivity.this);
                break;
            case R.id.item_detail_grid:
                gridDialog(DetailNoteActivity.this);
                break;
            case R.id.item_deyail_accept:
                updateNote();
                break;
            case R.id.item_detail_New:
                startActivity(new Intent(DetailNoteActivity.this, NewNoteActivity.class));
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
