package com.example.tam.appnotes.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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
import com.example.tam.appnotes.R;
import com.example.tam.appnotes.model.Note;
import com.example.tam.appnotes.presenter.CustomDialog;
import com.example.tam.appnotes.presenter.Database_Note;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewNoteActivity extends CustomDialog{
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

    public class ItemSelectedDate implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            SimpleDateFormat sdfomat = null;
            String strDateFormat = "dd/MM/yyyy";
            sdfomat = new SimpleDateFormat(strDateFormat);
            if(parent.getSelectedItemPosition() == 0) {
                mDate = mTxtCurrentDate.getText().toString().substring(0, 10);
                Toast.makeText(getApplicationContext(), mDate, Toast.LENGTH_LONG).show();
            }
            if(parent.getSelectedItemPosition() == 1) {
                mCalendar.add(Calendar.DAY_OF_YEAR, 1);
                mDate = sdfomat.format(mCalendar.getTime());
                Toast.makeText(getApplicationContext(), mDate, Toast.LENGTH_LONG).show();
                mCalendar = Calendar.getInstance();

            }
            if(parent.getSelectedItemPosition() == 2) {
                int weekday = mCalendar.get(Calendar.DAY_OF_WEEK);
                if (weekday!=Calendar.THURSDAY){
                    int days = (Calendar.SATURDAY - weekday + 5) % 7;
                    mCalendar.add(Calendar.DAY_OF_YEAR, days);
                    mDate = sdfomat.format(mCalendar.getTime());
                    Toast.makeText(getApplicationContext(), mDate, Toast.LENGTH_LONG).show();
                    mCalendar = Calendar.getInstance();
                }

            }
            if(parent.getSelectedItemPosition() == 3) {
                datePickerDialog(NewNoteActivity.this);
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
                timePickerDialog(NewNoteActivity.this);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

     public void insertNote() {
         try {
             mImgNewImage = (ImageView)findViewById(R.id.img_newImage);
             String newColor = String.valueOf(mNewColor);
             if(newColor != null){
             mDatabase.inSertNote(new Note(
                     mEdtTitle.getText().toString(),
                     mEdtNote.getText().toString(),
                     mDate.toString(),
                     mTxtCurrentDate.getText().toString(),
                     mNewColor,
                     ImageviewToBye(mImgNewImage)
             ));}
             else {
                 mDatabase.inSertNote(new Note(
                         mEdtTitle.getText().toString(),
                         mEdtNote.getText().toString(),
                         mDate.toString(),
                         mTxtCurrentDate.getText().toString(),
                         -258,
                         ImageviewToBye(mImgNewImage)));
             }
             Toast.makeText(getApplicationContext(), "Save successful", Toast.LENGTH_LONG).show();
         }catch (Exception e){
             Toast.makeText(getApplicationContext(), "Save erro", Toast.LENGTH_LONG).show();

         }

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
            case R.id.item_accept: {
                insertNote();
            }
            break;
            case R.id.item_grid: {
                gridDialog(NewNoteActivity.this);
            }
            break;
            case R.id.item_camera: {
                cameraDialog(NewNoteActivity.this);
            }
            break;
            default:
                break;
    }
        return super.onOptionsItemSelected(item);

    }

}
