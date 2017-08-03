package com.example.tam.appnotes.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.tam.appnotes.R;
import com.example.tam.appnotes.model.Camera;
import com.example.tam.appnotes.presenter.CustomAdapterCamere;
import com.example.tam.appnotes.presenter.Database_Note;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class NewNoteActivity extends AppCompatActivity {
    private TextView mTxtCurrentDate, mTxtAlarm;
    private EditText mEdtTitle, mEdtNote;
    private Spinner mSpnDate, mSpnTime;
    private LinearLayout mLinearView;
    private ImageView mImgNewImage;
    private java.util.Calendar mCalendar = java.util.Calendar.getInstance();
    private Dialog mDialogCamera, mDialogGrid;
    private ArrayAdapter<String> mAdapterDate, mAdapterTime;
    private String[] mDateName = {"Today", "Tomorrow", "NextWedesDay", "Other..."};
    private String[] mTimeName = {"09:00", "13:00", "17:00", "20:00", "Other..."};
    private ListView mLvCamera;
    private ArrayList<Camera> mArrayCamera;
    private static final int PICK_IMAGE = 100;
    private static final int REQUEST_CODE_CAMERA = 1;
    private Database_Note mDatabase;
    private String mDate, mTime;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        mTxtCurrentDate = (TextView) findViewById(R.id.txt_CurrentDate);
        mTxtAlarm = (TextView) findViewById(R.id.txt_Alarm);
        mEdtNote = (EditText)findViewById(R.id.edt_note);
        mEdtTitle = (EditText)findViewById(R.id.edt_title);
        mImgNewImage = (ImageView) findViewById(R.id.img_newImage);
        mSpnDate = (Spinner) findViewById(R.id.spn_Date);
        mSpnTime = (Spinner) findViewById(R.id.spn_Time);
        mLinearView = (LinearLayout) findViewById(R.id.linearView);
        mDatabase = new Database_Note(getApplicationContext());
        mTxtAlarm.setOnClickListener(new AlarmClick());
        mSpnDate.setOnItemSelectedListener(new ItemSelectedDate());
        mSpnTime.setOnItemSelectedListener(new ItemSelectedTime());
        getCurrentDate();
        ShowAlarm();
    }

    public class AlarmClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            mSpnDate.setVisibility(View.VISIBLE);
            mSpnTime.setVisibility(View.VISIBLE);
            mTxtAlarm.setVisibility(View.GONE);
        }
    }

    public class ItemSelectedDate implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (parent.getSelectedItemPosition() == 3) {
                DatePickerDialog();
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
                TimePickerDialog();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    public void DatePickerDialog() {
        DatePickerDialog datePicker = new DatePickerDialog(NewNoteActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        mDateName[3] = mDate;
                        mAdapterDate.notifyDataSetChanged();
                    }
                },
                mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH));
        datePicker.show();
    }

    public void TimePickerDialog() {
        TimePickerDialog timePicker = new TimePickerDialog(NewNoteActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mTime = hourOfDay + ":" + minute;
                        mTimeName[4] = mTime;
                        mAdapterTime.notifyDataSetChanged();
                    }
                },
                mCalendar.get(Calendar.HOUR_OF_DAY),
                mCalendar.get(Calendar.MINUTE), true);
        timePicker.show();
    }

    public void getCurrentDate() {
        SimpleDateFormat sdf = null;
        String strDateFormat = "dd/MM/yyyy HH:mm";
        sdf = new SimpleDateFormat(strDateFormat);
        mTxtCurrentDate.setText(sdf.format(mCalendar.getTime()));
    }

    public void GridDialog() {
        mDialogGrid = new Dialog(NewNoteActivity.this);
        mDialogGrid.setContentView(R.layout.dialog_grid);
        mDialogGrid.setTitle("Choose Color");
        ImageButton btnWhite = (ImageButton) mDialogGrid.findViewById(R.id.imgbtn_White);
        ImageButton btnYellow = (ImageButton) mDialogGrid.findViewById(R.id.imgbtn_Yellow);
        ImageButton btnBlue = (ImageButton) mDialogGrid.findViewById(R.id.imgbtn_blue);
        ImageButton btnGreen = (ImageButton) mDialogGrid.findViewById(R.id.imgbtn_green);
        btnWhite.setOnClickListener(new ChangeColor());
        btnYellow.setOnClickListener(new ChangeColor());
        btnBlue.setOnClickListener(new ChangeColor());
        btnGreen.setOnClickListener(new ChangeColor());
        mDialogGrid.show();
    }

    public void CameraDialog() {
        mDialogCamera = new Dialog(NewNoteActivity.this);
        mDialogCamera.setContentView(R.layout.dialog_camera);
        mDialogCamera.setTitle("Insert Picture");
        mLvCamera = (ListView) mDialogCamera.findViewById(R.id.lv_camera);
        mArrayCamera = new ArrayList<Camera>();
        mArrayCamera.add(new Camera("Take Photo", R.drawable.ic_action_camera_dark));
        mArrayCamera.add(new Camera("Choose Photo", R.drawable.ic_action_picture));
        CustomAdapterCamere adapterCamera = new CustomAdapterCamere(NewNoteActivity.this,
                R.layout.list_item_camera, mArrayCamera);
        mLvCamera.setAdapter(adapterCamera);
        mDialogCamera.show();
        mLvCamera.setOnItemClickListener(new ChoosePicture());
    }

    public class ChoosePicture implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (parent.getItemIdAtPosition(position) == 0) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
                mDialogCamera.dismiss();
            }
            if (parent.getItemIdAtPosition(position) == 1) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
                mDialogCamera.dismiss();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uriImage;
        if (resultCode == RESULT_OK & requestCode == PICK_IMAGE) {
            uriImage = data.getData();
            mImgNewImage.setImageURI(uriImage);
        }
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            mImgNewImage.setImageBitmap(bitmap);
        }
    }

    public class ChangeColor implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String colorImageButton = v.getTag().toString();
            int newColor = Color.parseColor(colorImageButton);
            switch (v.getId()) {
                case R.id.imgbtn_White:
                    mLinearView.setBackgroundColor(newColor);
                    mDialogGrid.dismiss();
                    break;
                case R.id.imgbtn_Yellow:
                    mLinearView.setBackgroundColor(newColor);
                    mDialogGrid.dismiss();
                    break;
                case R.id.imgbtn_green:
                    mLinearView.setBackgroundColor(newColor);
                    mDialogGrid.dismiss();
                    break;
                case R.id.imgbtn_blue:
                    mLinearView.setBackgroundColor(newColor);
                    mDialogGrid.dismiss();
                default:
                    break;
            }
        }
    }

    public void ShowAlarm() {
        mAdapterDate = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mDateName);
        mAdapterTime = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mTimeName);
        mAdapterDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAdapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpnDate.setAdapter(mAdapterDate);
        mSpnTime.setAdapter(mAdapterTime);
    }

    public void InsertNote() {
        String date = mTime + mDate;
        try {
            mDatabase.InsertNote(
                    mEdtTitle.getText().toString(),
                    mEdtNote.getText().toString(),
                    mDate.toString(),
                    mTxtCurrentDate.getText().toString(),
                    ImageviewToBye(mImgNewImage)
            );
            Toast.makeText(getApplicationContext(),"Save Successfull !", Toast.LENGTH_LONG).show();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Erro !", Toast.LENGTH_LONG).show();
        }
    }

    public byte[] ImageviewToBye(ImageView image) {
        BitmapDrawable drawable = (BitmapDrawable)image.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_accept: {
                InsertNote();
            }
            break;
            case R.id.item_grid: {
                GridDialog();
            }
            break;
            case R.id.item_camera: {
                CameraDialog();
            }
            break;
            default:
                break;
    }
        return super.onOptionsItemSelected(item);

    }

}
