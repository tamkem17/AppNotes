package com.example.tam.appnotes.presenter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
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
import com.example.tam.appnotes.ui.DetailNoteActivity;
import com.example.tam.appnotes.ui.NewNoteActivity;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by tam on 8/7/2017.
 */

public abstract class CustomDialog extends AppCompatActivity  {
    private static final int PICK_IMAGE = 100;
    private static final int REQUEST_CODE_CAMERA = 1;
    protected Dialog mDialogCamera, mDialogGrid;
    protected LinearLayout mLinearView;
    protected ListView mLvCamera;
    protected ArrayList<Camera> mArrayCamera;
    protected ImageView mImgNewImage;
    protected TextView mTxtCurrentDate;
    protected java.util.Calendar mCalendar = java.util.Calendar.getInstance();
    protected String[] mDateName = {"Today", "Tomorrow", "NextWedesDay", "Other..."};
    protected String[] mTimeName = {"09:00", "13:00", "17:00", "20:00", "Other..."};
    protected ArrayAdapter<String> mAdapterDate, mAdapterTime;
    protected Spinner mSpnDate, mSpnTime;
    protected String mDate, mTime;
    protected int mNewColor;



    public void gridDialog(Activity activity) {
        mDialogGrid = new Dialog(activity);
        mDialogGrid.setContentView(R.layout.dialog_grid);
        mDialogGrid.setTitle("Choose Color");
        ImageButton btnWhite = (ImageButton)mDialogGrid.findViewById(R.id.imgbtn_White);
        ImageButton btnYellow = (ImageButton) mDialogGrid.findViewById(R.id.imgbtn_Yellow);
        ImageButton btnBlue = (ImageButton) mDialogGrid.findViewById(R.id.imgbtn_blue);
        ImageButton btnGreen = (ImageButton) mDialogGrid.findViewById(R.id.imgbtn_green);
        btnWhite.setOnClickListener(new changeColor());
        btnYellow.setOnClickListener(new changeColor());
        btnBlue.setOnClickListener(new changeColor());
        btnGreen.setOnClickListener(new changeColor());
        mDialogGrid.show();
    }

    public void cameraDialog(Activity activity) {
        mDialogCamera = new Dialog(activity);
        mDialogCamera.setContentView(R.layout.dialog_camera);
        mDialogCamera.setTitle("Insert Picture");
        mLvCamera = (ListView) mDialogCamera.findViewById(R.id.lv_camera);
        mArrayCamera = new ArrayList<Camera>();
        mArrayCamera.add(new Camera("Take Photo", R.drawable.ic_action_camera_dark));
        mArrayCamera.add(new Camera("Choose Photo", R.drawable.ic_action_picture));
        CustomAdapterCamere adapterCamera = new CustomAdapterCamere(activity,
                R.layout.list_item_camera, mArrayCamera);
        mLvCamera.setAdapter(adapterCamera);
        mDialogCamera.show();
       mLvCamera.setOnItemClickListener(new choosePicture());
    }

    public void getCurrentDate() {
        mTxtCurrentDate = (TextView) findViewById(R.id.txt_CurrentDate);
        SimpleDateFormat sdf = null;
        String strDateFormat = "dd/MM/yyyy HH:mm";
        sdf = new SimpleDateFormat(strDateFormat);
        mTxtCurrentDate.setText(sdf.format(mCalendar.getTime()));
    }

   public TextWatcher inputTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            getSupportActionBar().setTitle(s);
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after){
        }
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };

    public void showAlarm() {
        mSpnDate = (Spinner) findViewById(R.id.spn_Date);
        mSpnTime = (Spinner) findViewById(R.id.spn_Time);
        mAdapterDate = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mDateName);
        mAdapterTime = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mTimeName);
        mAdapterDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAdapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpnDate.setAdapter(mAdapterDate);
        mSpnTime.setAdapter(mAdapterTime);
    }

    public void datePickerDialog() {
        DatePickerDialog datePicker = new DatePickerDialog(this,
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

    public void timePickerDialog() {
        TimePickerDialog timePicker = new TimePickerDialog(this,
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

    public class choosePicture implements AdapterView.OnItemClickListener {

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
        mImgNewImage = (ImageView)findViewById(R.id.img_newImage);
        if (resultCode == RESULT_OK & requestCode == PICK_IMAGE) {
            uriImage = data.getData();
            mImgNewImage.setImageURI(uriImage);
        }
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            mImgNewImage.setImageBitmap(bitmap);
        }
    }

    public class changeColor implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            mLinearView = (LinearLayout) findViewById(R.id.linearView);
            String colorImageButton = v.getTag().toString();
            mNewColor = Color.parseColor(colorImageButton);
            switch (v.getId()) {
                case R.id.imgbtn_White:
                    mLinearView.setBackgroundColor(mNewColor);
                    mDialogGrid.dismiss();
                    break;
                case R.id.imgbtn_Yellow:
                    mLinearView.setBackgroundColor(mNewColor);
                    mDialogGrid.dismiss();
                    break;
                case R.id.imgbtn_green:
                    mLinearView.setBackgroundColor(mNewColor);
                    mDialogGrid.dismiss();
                    break;
                case R.id.imgbtn_blue:
                    mLinearView.setBackgroundColor(mNewColor);
                    mDialogGrid.dismiss();
                default:
                    break;
            }
        }
    }

    public class ItemSelectedDate implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            SimpleDateFormat sdfomat = null;
            String strDateFormat = "dd/MM/yyyy";
            sdfomat = new SimpleDateFormat(strDateFormat);
            if(parent.getSelectedItemPosition() == 0) {
                mDate = mTxtCurrentDate.getText().toString().substring(0, 10);
            }
            if(parent.getSelectedItemPosition() == 1) {
                mCalendar.add(Calendar.DAY_OF_YEAR, 1);
                mDate = sdfomat.format(mCalendar.getTime());
                mCalendar = Calendar.getInstance();
            }
            if(parent.getSelectedItemPosition() == 2) {
                int weekday = mCalendar.get(Calendar.DAY_OF_WEEK);
                if (weekday!=Calendar.THURSDAY){
                    int days = (Calendar.SATURDAY - weekday + 5) % 7;
                    mCalendar.add(Calendar.DAY_OF_YEAR, days);
                    mDate = sdfomat.format(mCalendar.getTime());
                    mCalendar = Calendar.getInstance();
                }
            }
            if(parent.getSelectedItemPosition() == 3) {
                datePickerDialog();
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
                timePickerDialog();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

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
}
