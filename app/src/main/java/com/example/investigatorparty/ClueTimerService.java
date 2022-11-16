package com.example.investigatorparty;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class ClueTimerService extends IntentService {

    public static final String CLUE_TIMER_TICK_BROADCAST = "CLUE_TIMER_TICK";
    public static final String CLUE_TIMER_TIMEOUT_BROADCAST = "CLUE_TIME_TIMEOUT";

    private static Timer timer = new Timer();
    private int skillCheckDuration = 0;

    // Constructor required by IntentService
    public ClueTimerService() {
        super("ClueTimerService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        // TODO EXTRAs should be resources rather than hardcoded.
        boolean isStartTimerRequested = intent.getBooleanExtra("startTimer", false);
        int timerDuration = intent.getIntExtra("timerDuration", 1);
        Log.d("ClueTimerService", "Starting the timer");
        timer.scheduleAtFixedRate(new ClueTimerTask(timerDuration), 0, 1000);
    }

    // TimerTask to be executed when the timer elapses
    private class ClueTimerTask extends TimerTask
    {
        private int skillDuration;

        ClueTimerTask(int skillDuration)
        {
            this.skillDuration = skillDuration;
        }

        public void run()
        {
            Log.d("ClueTimerService", "Timer elapsed");

            // Broadcast that a second has elapsed
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
            Intent localIntent = new Intent();

            if (skillDuration <=0)
            {
                localIntent.setAction(CLUE_TIMER_TIMEOUT_BROADCAST);
                cancel();
            }
            else
            {
                this.skillDuration = skillDuration - 1;
                localIntent.setAction(CLUE_TIMER_TICK_BROADCAST);
                localIntent.putExtra("timeRemaining", skillDuration);
            }
            localBroadcastManager.sendBroadcast(localIntent);
        }
    }
}
