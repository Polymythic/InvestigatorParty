package com.example.investigatorparty;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

public class SkillCheckActivity extends AppCompatActivity {

    // Get an instance of the database
    private AppDatabase db;
    private InvestigationDatabaseService databaseService;
    private GameEventTransitionModel originatingTransitionForSkillCheck;
    private int selectedCharacterId = SettingsActivity.DEFAULT_SELECTED_CHARACTER_ID;

    private ServiceConnection databaseServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            InvestigationDatabaseService.DatabaseServiceBinder databaseBinder = (InvestigationDatabaseService.DatabaseServiceBinder) service;
            databaseService = databaseBinder.getService();
            Log.i("SkillCheckActivity", "onServiceConnected()");

            loadResourcePreferences();

            // Determine which intent started the skill check and what is required
            Intent intent = getIntent();
            int getGameEventTransitionId = intent.getIntExtra("gameEventTransitionId", 0);

            originatingTransitionForSkillCheck = databaseService.getDb().gameEventTransitionDao().getGameEventTransitionById(getGameEventTransitionId);
            TextView skillCheckName = (TextView) findViewById(R.id.skillCheckName);
            RawCharacterSkillModel requiredSkill = databaseService.getDb().rawCharacterSkillDao().findById(originatingTransitionForSkillCheck.getSkillRequired());
            skillCheckName.setText(requiredSkill.getRawSkillName());

            // Start the check timer
            Intent skillTimerIntent = new Intent(getApplicationContext(), ClueTimerService.class);
            skillTimerIntent.putExtra("startTimer", true);
            skillTimerIntent.putExtra("timerDuration", originatingTransitionForSkillCheck.getSkillCheckDuration());
            startService(skillTimerIntent);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_check);

        Intent intent = new Intent(this, InvestigationDatabaseService.class);
        bindService(intent, databaseServiceConnection, Context.BIND_AUTO_CREATE);
        Log.i("DATABASE", "SkillCheckActivity binding to database service...");
    }

    private BroadcastReceiver listener = new BroadcastReceiver() {
        @Override
        public void onReceive( Context context, Intent intent ) {
            if (intent.getAction() == ClueTimerService.CLUE_TIMER_TICK_BROADCAST)
            {
                Log.d("SkillCheckActivity", "Received countdown update");
                // TODO this should not be a hard coded string.. resource
                int timeRemaining = intent.getIntExtra("timeRemaining", 0);
                TextView timeRemainingActivity = (TextView) findViewById(R.id.timeRemainingText);
                timeRemainingActivity.setText(Integer.toString(timeRemaining) + "s");
            }
            else if (intent.getAction() == ClueTimerService.CLUE_TIMER_TIMEOUT_BROADCAST) {

                // Generate the skill percentile roll (0-99 + 1)
                Random randomGen = new Random();
                int dieRoll = randomGen.nextInt(99);
                dieRoll += 1;

                // Get the character's skill level
                CharacterSkillModel characterSkillRequired = databaseService.getDb().characterSkillDao().getSkillForCharacter(selectedCharacterId, originatingTransitionForSkillCheck.getSkillRequired());
                int targetRollNumber = characterSkillRequired.getSkillValue();

                // Adjust for item skill modifiers
                int itemsSkillModifier = 0;
                List<GameItemModel> itemsModifyingSkillCheck = databaseService.getDb().gameItemDao().getItemsForCharacterThatModifySkill(originatingTransitionForSkillCheck.getSkillRequired(), selectedCharacterId);

                for (GameItemModel item : itemsModifyingSkillCheck)
                {
                    itemsSkillModifier += item.getSkillCheckModifier();
                }
                int adjustedDieRoll = dieRoll - itemsSkillModifier;

                int modifiedTargetNumber = targetRollNumber - originatingTransitionForSkillCheck.getSkillDifficultyModifier();

                Log.d("SKILL", "Die Roll: " + Integer.toString(dieRoll));
                Log.d("SKILL", "Items Modifier:  " + Integer.toString(itemsSkillModifier));
                Log.d("SKILL", "Adjusted Die Roll:  " + Integer.toString(adjustedDieRoll));
                Log.d("SKILL", "Target Roll (Base Skill Level):  " + Integer.toString(targetRollNumber));
                Log.d("SKILL", "Modified Target by difficulty:  " + Integer.toString(modifiedTargetNumber));

                if (adjustedDieRoll < modifiedTargetNumber) {
                    Toast toast = Toast.makeText(getApplicationContext(), "You were sucessful! Target: " + Integer.toString(modifiedTargetNumber) + " Rolled:" + Integer.toString(adjustedDieRoll), Toast.LENGTH_LONG);
                    toast.show();
                    passSkillCheck(null);
                }
                else
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "You were not able to succeed. Target: " + Integer.toString(modifiedTargetNumber) + " Rolled:" + Integer.toString(adjustedDieRoll), Toast.LENGTH_LONG);
                    toast.show();
                    failSkillCheck(null);
                }
            }

        }
    };

    @Override
    protected void onResume()
    {
        super.onResume();
        loadResourcePreferences();

        // Register our local broadcast receiver in case the ClueTimer sends countdown events
        IntentFilter broadcastIntentfilter = new IntentFilter();
        broadcastIntentfilter.addAction(ClueTimerService.CLUE_TIMER_TIMEOUT_BROADCAST);
        broadcastIntentfilter.addAction(ClueTimerService.CLUE_TIMER_TICK_BROADCAST);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(listener, broadcastIntentfilter);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(listener);
    }

    public void quitSkillCheck(View view)
    {
        // TODO stop any running skill check timer
        Intent intent = new Intent(getApplicationContext(), ClueActivity.class);
        startActivity(intent);
    }

    public void passSkillCheck(View view)
    {
        Intent intent = new Intent(getApplicationContext(), ClueActivity.class);
        intent.putExtra("gameEventId", originatingTransitionForSkillCheck.getEventIdIfSkillCheckPass());
        startActivity(intent);
    }

    public void failSkillCheck(View view)
    {
        Intent intent = new Intent(getApplicationContext(), ClueActivity.class);
        intent.putExtra("gameEventId", originatingTransitionForSkillCheck.getEventIdIfSkillCheckFail());
        startActivity(intent);
    }

    protected void loadResourcePreferences() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        selectedCharacterId = sharedPref.getInt(getString(R.string.preferenceSelectedInvestigator), SettingsActivity.DEFAULT_SELECTED_CHARACTER_ID);
    }


}
