package com.example.tam.appnotes.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;

import com.example.tam.appnotes.R;
import com.example.tam.appnotes.model.Note;
import com.example.tam.appnotes.presenter.CustomAdapterPicture;
import com.example.tam.appnotes.presenter.CommonActivity;
import com.example.tam.appnotes.presenter.Database_Note;

public class DetailNoteActivity extends CommonActivity {
    private TextView mTxtAlarm;
    private Spinner mSpnDate, mSpnTime;
    private ImageButton mImbtnCancel;
    private int mIdNote, mPositionArray;
    private EditText mEdtTitle, mEdtNote;
    private ImageView mImgNote;
    private Database_Note mDatabase;
    private BottomNavigationView mBottomBar;
    private ScrollView mScoll;
    private ArrayList<Note> mArrayNote;
    private String mUpdateTime = "", mUpdateDate = "";

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
        mImgNote = (ImageView)findViewById(R.id.img_picture);
        mSpnDate = (Spinner) findViewById(R.id.spn_Date);
        mSpnTime = (Spinner) findViewById(R.id.spn_Time);
        mScoll = (ScrollView) findViewById(R.id.scView);
        mTxtAlarm.setOnClickListener(new alarmClick());
        mImbtnCancel.setOnClickListener(new canCelClick());
        mSpnDate.setOnItemSelectedListener(new ItemSelectedDate());
        mSpnTime.setOnItemSelectedListener(new ItemSelectedTime());
        Intent intent = getIntent();
        mArrayNote = (ArrayList<Note>)intent.getSerializableExtra("arrayNote");
        mIdNote = intent.getExtras().getInt("idNote");
        mPositionArray = intent.getExtras().getInt("position");
        mEdtTitle.addTextChangedListener(inputTextWatcher);
        mBottomBar = (BottomNavigationView) findViewById(R.id.btnvg_BottomBar);
        disableShiftMode(mBottomBar);
        mBottomBar.setOnNavigationItemSelectedListener(new lectesedItemBottom());
        getCurrentDate();
        showAlarm();
        getNoteId();
    }

    public void shareNote(){
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/html");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                Html.fromHtml("<p>"+ mEdtTitle.getText().toString() + " : " + mEdtNote.getText().toString() + "</p>"));
        startActivity(Intent.createChooser(sharingIntent,"Share with"));
    }

    public void dialogDeleteNote(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Confirm Delete");
        alertDialogBuilder.setMessage("Are you sure you want to delete this");
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                mDatabase.deleteNote(mIdNote);
                startActivity(new Intent(DetailNoteActivity.this, MainActivity.class));
                finish();
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

    public void getNoteId(){
        mGrvPicture = (GridView)findViewById(R.id.grv_picture);
        Cursor cursor = mDatabase.getNoteId(mIdNote);
        while (cursor.moveToNext()) {
            mEdtTitle.setText(cursor.getString(1));
            mEdtNote.setText(cursor.getString(2));
            mScoll.setBackgroundColor(cursor.getInt(5));
            String listPicturePath = cursor.getString(6);
            mArrayPicture = new ArrayList<String>();
            String[] x =listPicturePath.split(", ");
            for (int i = 0; i < x.length; i++){
               String s = x[i].toString();
                mArrayPicture.add(s);
            }
            mArrayPicture.toString();
            if(listPicturePath.equals("")) {
                mGrvPicture.setVisibility(View.GONE);
            }
            else {
                mGrvPicture.setVisibility(View.VISIBLE);
                mAdapterPicture = new CustomAdapterPicture(this, R.layout.list_item_picture, mArrayPicture);
                mGrvPicture.setAdapter(mAdapterPicture);
            }
            if(cursor.getString(3).equals("")) {
                mTxtAlarm.setVisibility(View.VISIBLE);
                mSpnDate.setVisibility(View.GONE);
                mSpnTime.setVisibility(View.GONE);
            }else {
                mPositionTime = mTimeName.length - 1;
                mPositionDate = mDateName.length - 1;
                mUpdateDate = cursor.getString(3).substring(0, 10);
                mUpdateTime = cursor.getString(3).substring(10, 15);
                mDateName[3] = mUpdateDate;
                mTimeName[4] = mUpdateTime;
                mSpnDate.setSelection(mPositionDate);
                mSpnTime.setSelection(mPositionTime);
                mTxtAlarm.setVisibility(View.GONE);
                mSpnDate.setVisibility(View.VISIBLE);
                mSpnTime.setVisibility(View.VISIBLE);
            }
        }
    }

    public void getNoteIdNext() {
        mPositionArray ++;
        if(mPositionArray < mArrayNote.size()){
            mIdNote = mArrayNote.get(mPositionArray).getIdNote();
            getNoteId();
        }else {
            mPositionArray --;
            getNoteId();
        }
    }

    public void getNoteidPrevious() {
        mPositionArray --;
        if(mPositionArray >= 0) {
            mIdNote = mArrayNote.get(mPositionArray).getIdNote();
            getNoteId();
        }else {
            mPositionArray ++;
            getNoteId();
        }
    }

    public void updateNote(){
        String listPicturePath = mArrayPicture.toString();
        String pictuePath = listPicturePath.substring(1, listPicturePath.length() - 1);
        String alarm;
        if(mDate == ""){
            alarm = mUpdateDate + mTime;
        }
       else if(mTime == ""){
            alarm = mDate + mUpdateTime;
        }else if(mDate == "" && mTime == ""){
            alarm = mUpdateDate + mUpdateTime;
        }
        else {
            alarm = mDate + mTime;
        }
        try {
            mDatabase.updateNote(mIdNote, new Note(
                    mEdtTitle.getText().toString(),
                    mEdtNote.getText().toString(),
                    alarm,
                    mTxtCurrentDate.getText().toString(),
                    mNewColor,
                    pictuePath));
            startActivity(new Intent(DetailNoteActivity.this, MainActivity.class));
            finish();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Erro Update", Toast.LENGTH_LONG).show();
        }
    }

    public void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
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



    public class lectesedItemBottom implements BottomNavigationView.OnNavigationItemSelectedListener{

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.item_prvious:
                    getNoteidPrevious();
                    break;
                case R.id.item_delete:
                    dialogDeleteNote();
                    break;
                case R.id.item_share:
                    shareNote();
                    break;
                case R.id.item_next:
                    getNoteIdNext();
                    break;
                default:
                    break;
            }
            return true;
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
                finish();
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
