package com.example.tam.appnotes.presenter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.tam.appnotes.Manifest;
import com.example.tam.appnotes.R;
import com.example.tam.appnotes.model.Camera;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by tam on 8/7/2017.
 */

public abstract class CustomDialog extends AppCompatActivity  {
    private static final int PICK_IMAGE = 100;
    private static final int REQUEST_CODE_CAMERA = 1;
    private static final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    protected Dialog mDialogCamera, mDialogGrid;
    protected ScrollView mScroll;
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
        mDialogGrid = new Dialog(activity, R.style.Dialog);
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
        mDialogCamera = new Dialog(activity, R.style.Dialog);
        mDialogCamera.setContentView(R.layout.dialog_camera);
        mDialogCamera.setTitle("Insert Picture");
        mLvCamera = (ListView) mDialogCamera.findViewById(R.id.lv_camera);
        mArrayCamera = new ArrayList<Camera>();
        mArrayCamera.add(new Camera("Take Photo", R.drawable.ic_action_camera_dark));
        mArrayCamera.add(new Camera("Choose Photo", R.drawable.ic_action_picture));
        CustomAdapterCamere adapterCamera = new CustomAdapterCamere(activity, R.layout.list_item_camera, mArrayCamera);
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkPermission();
                }else {
                    takePhoto();
                }
            }
            if (parent.getItemIdAtPosition(position) == 1) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
                mDialogCamera.dismiss();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_MULTIPLE_REQUEST:
                if (grantResults.length > 0) {
                    boolean cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(cameraPermission && readExternalFile)
                    {
                        takePhoto();
                    } else {
                        Snackbar.make(this.findViewById(android.R.id.content),
                                "Please Grant Permissions to upload profile photo",
                                Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        requestPermissions(
                                                new String[]{android.Manifest.permission
                                                        .READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA},
                                                PERMISSIONS_MULTIPLE_REQUEST);
                                    }
                                }).show();
                    }
                }
                break;
        }
    }

    public void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getApplication(), "com.example.tam.appnotes.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                List<ResolveInfo> resInfoList = getApplicationContext().getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    getApplicationContext().grantUriPermission(packageName, photoURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                startActivityForResult(takePictureIntent, REQUEST_CODE_CAMERA);
            }
        }
        mDialogCamera.dismiss();
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir);
        mPicturePath = image.getAbsolutePath();
        return image;
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) + ContextCompat
                .checkSelfPermission(this,
                        android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (this, android.Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (this, android.Manifest.permission.CAMERA)) {

                Snackbar.make(this.findViewById(android.R.id.content),
                        "Please Grant Permissions to upload profile photo",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestPermissions(
                                        new String[]{android.Manifest.permission
                                                .READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA},
                                        PERMISSIONS_MULTIPLE_REQUEST);
                            }
                        }).show();
            } else {
                requestPermissions(
                        new String[]{android.Manifest.permission
                                .READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA},
                        PERMISSIONS_MULTIPLE_REQUEST);
            }
        } else {
            takePhoto();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mGrvPicture = (GridView)findViewById(R.id.grv_picture);
        mGrvPicture.setVisibility(View.VISIBLE);
        if(resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE) {
                Uri uriImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(uriImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mPicturePath = cursor.getString(columnIndex);
                cursor.close();
                mArrayPicture.add(mPicturePath);
            }
            if (requestCode == REQUEST_CODE_CAMERA && data != null) {
                mArrayPicture.add(mPicturePath);
            }
        }
        mAdapterPicture = new CustomAdapterPicture(this, R.layout.list_item_picture, mArrayPicture);
        mGrvPicture.setAdapter(mAdapterPicture);
    }

    public class changeColor implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            mScroll = (ScrollView) findViewById(R.id.scView);
            String colorImageButton = v.getTag().toString();
            mNewColor = Color.parseColor(colorImageButton);
            switch (v.getId()) {
                case R.id.imgbtn_White:
                    mScroll.setBackgroundColor(mNewColor);
                    mDialogGrid.dismiss();
                    break;
                case R.id.imgbtn_Yellow:
                    mScroll.setBackgroundColor(mNewColor);
                    mDialogGrid.dismiss();
                    break;
                case R.id.imgbtn_green:
                    mScroll.setBackgroundColor(mNewColor);
                    mDialogGrid.dismiss();
                    break;
                case R.id.imgbtn_blue:
                    mScroll.setBackgroundColor(mNewColor);
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
