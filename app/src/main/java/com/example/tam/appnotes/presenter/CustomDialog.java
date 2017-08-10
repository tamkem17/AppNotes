package com.example.tam.appnotes.presenter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.example.tam.appnotes.R;
import com.example.tam.appnotes.model.Camera;
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
    protected TextView mTxtCurrentDate;
    protected java.util.Calendar mCalendar = java.util.Calendar.getInstance();
    protected String[] mDateName = {"Today", "Tomorrow", "NextWedesDay", "Other..."};
    protected String[] mTimeName = {"09:00", "13:00", "17:00", "20:00", "Other..."};
    protected ArrayAdapter<String> mAdapterDate, mAdapterTime;
    protected Spinner mSpnDate, mSpnTime;
    protected String mDate = "", mTime = "", mPicturePath = "";
    protected int mNewColor;
    protected GridView mGrvPicture;
    protected CustomAdapterPicture mAdapterPicture;
    protected ArrayList<String> mArrayPicture = new ArrayList<>();

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
        mGrvPicture = (GridView)findViewById(R.id.grv_picture);
        mGrvPicture.setVisibility(View.VISIBLE);
            if (requestCode == PICK_IMAGE  && resultCode == RESULT_OK) {
                Uri uriImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(uriImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mPicturePath = cursor.getString(columnIndex);
                cursor.close();
            }
            if (requestCode == REQUEST_CODE_CAMERA && data != null) {
                String[] projection = { MediaStore.Images.Media.DATA };
                Cursor cursor = managedQuery(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        projection, null, null, null);
                int column_index_data = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToLast();
                mPicturePath = cursor.getString(column_index_data);
                Toast.makeText(getApplicationContext(), mPicturePath, Toast.LENGTH_LONG).show();
            }
        mArrayPicture.add(mPicturePath.toString());
        mAdapterPicture = new CustomAdapterPicture(this, R.layout.list_item_picture, mArrayPicture);
        mGrvPicture.setAdapter(mAdapterPicture);
        mAdapterPicture.notifyDataSetChanged();
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
            /*Ngay tiếp theo*/
            if(parent.getSelectedItemPosition() == 1) {
                mCalendar.add(Calendar.DAY_OF_YEAR, 1);
                mDate = sdfomat.format(mCalendar.getTime());
                mCalendar = Calendar.getInstance();
            }
            /*thứ 5 tuần tiếp theo */
            if(parent.getSelectedItemPosition() == 2) {
                int weekday = mCalendar.get(Calendar.DAY_OF_WEEK);
                if (weekday!=Calendar.THURSDAY){
                    int days = (Calendar.SATURDAY - weekday + 5) % 7;
                    mCalendar.add(Calendar.DAY_OF_YEAR, days);
                    mDate = sdfomat.format(mCalendar.getTime());
                    mCalendar = Calendar.getInstance();
                }else {
                    mCalendar.add(Calendar.DAY_OF_YEAR, 7);
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
           switch (parent.getSelectedItemPosition()){
               case 0:
                   mTime = mTimeName[0];
                   break;
               case 1:
                   mTime = mTimeName[1];
                   break;
               case 2:
                   mTime = mTimeName[2];
                   break;
               case 3:
                   mTime = mTimeName[3];
                   break;
               case 4:
                   timePickerDialog();
                   break;
               default:
                   break;
           }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
