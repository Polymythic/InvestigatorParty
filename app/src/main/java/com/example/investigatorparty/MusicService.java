package com.example.investigatorparty;

import android.app.IntentService;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;

public class MusicService extends IntentService {

    MediaPlayer mediaPlayer;

    // Constructor is required for an IntentService
    public MusicService() {
        super("MusicService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        // TODO EXTRAs should be resources rather than hardcoded.
        boolean isStartMusicRequested = intent.getBooleanExtra("startMusic", false);

        if (isStartMusicRequested) {
            if ((mediaPlayer!= null) && (mediaPlayer.isPlaying()))
            {
                Log.d("MusicService", "Music has been requested and should be playing.");
            }
            else {
                Log.d("MusicService", "Music has been requested and should play.");
                int resID = getResources().getIdentifier("horrortheme", "raw", getPackageName());
                mediaPlayer = MediaPlayer.create(this, resID);
                mediaPlayer.start();
            }
        }
        else
        {
            Log.d("MusicService", "Music has been requested to stop.");

            if (mediaPlayer != null)
                mediaPlayer.stop();
        }
    }

//    @Override
//    public IBinder onBind(Intent intent) {
//        Log.d("MusicService", "Service has been binded to.");
//
//        boolean isStartMusicRequested = intent.getBooleanExtra("startMusic", false);
//
//        if (isStartMusicRequested) {
//            if ((mediaPlayer!= null) && (mediaPlayer.isPlaying()))
//            {
//                Log.d("MusicService", "Music has been requested and should be playing.");
//            }
//            else {
//                Log.d("MusicService", "Music has been requested and should play.");
//                int resID = getResources().getIdentifier("horrortheme", "raw", getPackageName());
//                mediaPlayer = MediaPlayer.create(this, resID);
//                mediaPlayer.start();
//            }
//        }
//        else
//        {
//            Log.d("MusicService", "Music has been requested to stop.");
//
//            if (mediaPlayer != null)
//                mediaPlayer.stop();
//        }
//    }
};