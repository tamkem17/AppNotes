package com.example.tam.appnotes.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;

import com.example.tam.appnotes.R;
import com.example.tam.appnotes.model.Note;
import com.example.tam.appnotes.presenter.CustomAdapterPicture;
import com.example.tam.appnotes.presenter.CustomDialog;
import com.example.tam.appnotes.presenter.Database_Note;

public class DetailNoteActivity extends CustomDialog {
    private TextView mTxtAlarm;
    private Spinner mSpnDate, mSpnTime;
    private ImageButton mImbtnCancel;
    private int mIdNote, mPositionArray;
    private EditText mEdtTitle, mEdtNote;
    private ImageView mImgNote;
    private Database_Note mDatabase;
    private BottomNavigationView mBottomBar;
    private LinearLayout mLinearView;
    private ArrayList<Note> mArrayNote;

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
        mLinearView = (LinearLayout) findViewById(R.id.linearView);
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
        //mArrayPicture = new ArrayList<Note>();
        Cursor cursor = mDatabase.getNoteId(mIdNote);
        while (cursor.moveToNext()) {
            mEdtTitle.setText(cursor.getString(1));
            mEdtNote.setText(cursor.getString(2));
            mLinearView.setBackgroundColor(cursor.getInt(6));
            String listPicturePath = cursor.getString(7);
            if(listPicturePath.equals("")) {
                mGrvPicture.setVisibility(View.GONE);
            }
            else {
                mGrvPicture.setVisibility(View.VISIBLE);
                //mArrayPicture.add(new Note(listPicturePath));
               // mAdapterPicture = new CustomAdapterPicture(this, R.layout.list_item_picture, mArrayPicture);
                mGrvPicture.setAdapter(mAdapterPicture);
            }
            if(cursor.getString(3).equals("")) {
                mTxtAlarm.setVisibility(View.VISIBLE);
                mSpnDate.setVisibility(View.GONE);
                mSpnTime.setVisibility(View.GONE);
            }else {
                mDateName[3] = cursor.getString(3);
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
        try {
            mDatabase.updateNote(mIdNote, new Note(
                    mEdtTitle.getText().toString(),
                    mEdtNote.getText().toString(),
                    mDate.toString(),
                    mTime.toString(),
                    mTxtCurrentDate.getText().toString(),
                    mNewColor,
                    mPicturePath.toString()));
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
