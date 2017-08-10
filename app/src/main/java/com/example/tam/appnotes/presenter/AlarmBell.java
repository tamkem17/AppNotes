package com.example.tam.appnotes.presenter;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.example.tam.appnotes.R;

/**
 * Created by tam on 8/10/2017.
 */

public class AlarmBell extends Service {
    private MediaPlayer mMediaPlayer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mMediaPlayer = MediaPlayer.create(this, R.raw.nhacchuong);
        mMediaPlayer.start();
        return START_NOT_STICKY;
    }
}
