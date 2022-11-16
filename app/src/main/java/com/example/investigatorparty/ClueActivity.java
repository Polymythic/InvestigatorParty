package com.example.investigatorparty;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

public class ClueActivity extends AppCompatActivity {

    // @TODO Refactor this class.  Its becoming very busy.

    // Get an instance of the database
    private AppDatabase db;
    private InvestigationDatabaseService databaseService;
    private int currentlyDisplayedEvent = 0;
    private int selectedCharacterId = SettingsActivity.DEFAULT_SELECTED_CHARACTER_ID;

    private ServiceConnection databaseServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            InvestigationDatabaseService.DatabaseServiceBinder databaseBinder = (InvestigationDatabaseService.DatabaseServiceBinder) service;
            databaseService = databaseBinder.getService();
            Log.i("ClueActivity", "onServiceConnected()");

            // Determine if we are entering the activity because of a transition
            Intent intent = getIntent();
            int newRequestedGameEventId = intent.getIntExtra("gameEventId", 0);

            loadResourcePreferences();

            // This activity is requested to activate gameEvent
            if (newRequestedGameEventId != 0) {
                processNewGameEvent(newRequestedGameEventId);
            }
            // Always update the display
            displayCurrentGameEvent();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clue);

        Intent intent = new Intent(this, InvestigationDatabaseService.class);
        bindService(intent, databaseServiceConnection, Context.BIND_AUTO_CREATE);
        Log.i("DB", "Binding to database service...");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_clues);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Load the saved preferences
        loadResourcePreferences();
    }


    public void handleClueCodeEntered(View view) {
        EditText clueCodeEditText = (EditText) findViewById(R.id.clueCodeValue);
        try {
            int clueCode = Integer.parseInt(clueCodeEditText.getText().toString());
            processNewGameEvent(clueCode);
        } catch (NumberFormatException e) {
            currentlyDisplayedEvent = 0;
        }

        displayCurrentGameEvent();
    }

    private void processNewGameEvent(int gameEventId) {

        Log.i("EVENT", "Processing a new clue code: " + Integer.toString(gameEventId));
        GameEventModel matchingGameModel = databaseService.getDb().gameEventDao().getEventByClueCode(gameEventId);
        String eventSound = "sound_clue";

        if (matchingGameModel == null) {
            currentlyDisplayedEvent = 0;
        } else {
            // Log the new event in the journal
            logJournalEntry(selectedCharacterId, gameEventId);
            CharacterModel selectedCharacter = databaseService.getDb().characterDao().findById(selectedCharacterId);

            // @TODO if you don't have the right item
            // @TODO or you don't have the right event
            int hitPointLoss = matchingGameModel.getHealthLossForEvent();
            if (hitPointLoss > 0)
            {
                int hitPoints = selectedCharacter.getHitPoints();
                hitPoints = hitPoints - hitPointLoss;
                selectedCharacter.setHitPoints(hitPoints);
                databaseService.getDb().characterDao().update(selectedCharacter);

                eventSound = "sound_healthscream";

                if (hitPoints <= 0) {
                    Intent intent = new Intent(getApplicationContext(), FateActivity.class);
                    startActivity(intent);
                }
            }

            // @TODO if you don't have the right item
            // @TODO or you don't have the right event
            // @TODO need to have an update DAO
            int sanityPointLoss = matchingGameModel.getSanityLossForEvent();
            if (sanityPointLoss > 0) {
                int sanityPoints = selectedCharacter.getSanityPoints();
                sanityPoints = sanityPoints - sanityPointLoss ;
                selectedCharacter.setSanityPoints(sanityPoints);
                databaseService.getDb().characterDao().update(selectedCharacter);

                eventSound = "sound_insanityscream";

                if (sanityPoints <= 0) {
                    Intent intent = new Intent(getApplicationContext(), FateActivity.class);
                    startActivity(intent);
                }
            }

            // @TODO Item with null is being set to 0 as an int, but the primary key may be 0.
            Log.d("ITEM", "Event Item: " + Integer.toString(matchingGameModel.getItemGrantedForEvent()));
            if (matchingGameModel.getItemGrantedForEvent() != 0) {
                InventoryTrackerModel newInventoryItem = new InventoryTrackerModel();
                newInventoryItem.setCharacterId(selectedCharacterId);
                newInventoryItem.setItemId(matchingGameModel.getItemGrantedForEvent());
                databaseService.getDb().inventoryTrackerDao().insertAll(newInventoryItem);

                eventSound = "sound_itemfound";
            }

            //@TODO mark the event as "done" if it is tracked

            // Update this as the current event
            playMediaSound(eventSound);
            currentlyDisplayedEvent = gameEventId;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveResourcePreferences();
    }

    private void displayCurrentGameEvent() {
        TextView clueName = (TextView) findViewById(R.id.clueName);
        TextView clueDescription = (TextView) findViewById(R.id.clueDescription);

        // Remove all prior transition views
        TableLayout skillsTableLayout = (TableLayout) findViewById(R.id.skillCheckTable);
        skillsTableLayout.removeAllViews();

        if (currentlyDisplayedEvent == 0) {
            clueName.setText("No Current Clue");
            clueDescription.setText("Please enter a valid Clue Code.");
        } else {

            GameEventModel matchingGameModel = databaseService.getDb().gameEventDao().getEventByClueCode(currentlyDisplayedEvent);
            clueName.setText(matchingGameModel.getEventName());
            clueDescription.setText(matchingGameModel.getEventDescription());

            // Get all transitions associated with the clue if there are any
            List<GameEventTransitionModel> transitionsFromEvent = databaseService.getDb().gameEventTransitionDao().getAllTransitionsForEvent(currentlyDisplayedEvent);

            // Add event transitions for each event
            for (GameEventTransitionModel transition : transitionsFromEvent) {
                Log.i("CLUE", "Found a transition");
                // @TODO - should check all of the destination events to see if they are tracked
                // and already marked as complete.  If so, do not display them in the collection

                // Check if the player has the required item to enable transition
                if (transition.getItemIdRequiredToEnable() != 0) {
                    List<InventoryTrackerModel> listofMatchingInventory = databaseService.getDb().inventoryTrackerDao().itemAssignedToCharacter(selectedCharacterId, transition.getItemIdRequiredToEnable());
                    if (listofMatchingInventory.isEmpty()) {
                        continue;
                    }
                }

                // Create a new row to display the needed skill check
                TableRow newRow = new TableRow(this);
                RawCharacterSkillModel rawSkillRequired = databaseService.getDb().rawCharacterSkillDao().findById(transition.getSkillRequired());

                Button skillCheckNameButton = new Button(this);
                skillCheckNameButton.setText(rawSkillRequired.getRawSkillName());
                skillCheckNameButton.setTextSize(20);
                skillCheckNameButton.setGravity(Gravity.LEFT);
                // Set the id of the button to the gameEventTransition so we can look it up
                skillCheckNameButton.setId(transition.getGameEventTransitionId());
                skillCheckNameButton.setOnClickListener(onSkillCheckButtonClicked(skillCheckNameButton));
                TextView skillCheckValue = new TextView(this);

                String difficultyDescription;
                int skillDifficultyModifier = transition.getSkillDifficultyModifier();

                // @TODO - Skill Check
                // The skil check description should be based on how likely THIS investigator
                // is to make it
                // @TODO Consider making the duration longer for less skilled investigators
                if (skillDifficultyModifier > 25) {
                    difficultyDescription = "Extreme";
                } else if (skillDifficultyModifier > 10) {
                    difficultyDescription = "Hard";
                } else if (skillDifficultyModifier > 0) {
                    difficultyDescription = "Above Average";
                } else {
                    difficultyDescription = "Average";
                }

                skillCheckValue.setText(difficultyDescription);
                skillCheckValue.setTextSize(20);
                skillCheckValue.setGravity(Gravity.CENTER);
                TextView skillCheckDuration = new TextView(this);
                skillCheckDuration.setText(Integer.toString(transition.getSkillCheckDuration()));
                skillCheckDuration.setTextSize(20);
                skillCheckDuration.setGravity(Gravity.RIGHT);
                newRow.addView(skillCheckNameButton);
                newRow.addView(skillCheckValue);
                newRow.addView(skillCheckDuration);

                if (skillsTableLayout != null)
                    skillsTableLayout.addView(newRow);
            }
        }
    }

    // When a skill check button is pressed, start the skill check activity
    // and pass an intent with the id and value that is tested
    View.OnClickListener onSkillCheckButtonClicked(final Button button) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SkillCheckActivity.class);
                intent.putExtra("gameEventTransitionId", button.getId());
                startActivity(intent);
            }
        };
    }

    protected void saveResourcePreferences() {
        SharedPreferences sharedPreferenceFile = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferenceFile.edit();

        editor.putInt(getString(R.string.preferenceDisplayedEventId), currentlyDisplayedEvent);
        Log.d("PREFERENCES", "ClueActivity - Saving : " + getString(R.string.preferenceDisplayedEventId) + " Value: " + Integer.toString(currentlyDisplayedEvent));
        editor.commit();
    }

    protected void loadResourcePreferences() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        selectedCharacterId = sharedPref.getInt(getString(R.string.preferenceSelectedInvestigator), SettingsActivity.DEFAULT_SELECTED_CHARACTER_ID);
        currentlyDisplayedEvent = sharedPref.getInt(getString(R.string.preferenceDisplayedEventId), 0);
    }

    private void logJournalEntry(int characterId, int clueCode) {
        // Write the new event into the journals
        Log.i("JOURNAL", "Logging new journal entry");
        JournalTrackerDao journalTracker = databaseService.getDb().journalTrackerDao();

        // Log the event if it does not exist
        if (journalTracker.getEventsForCharacterByGameId(characterId, clueCode).isEmpty())
        {
            JournalTrackerModel newJournalEntry = new JournalTrackerModel();
            newJournalEntry.setCharacterId(selectedCharacterId);
            newJournalEntry.setGameEventId(clueCode);
            journalTracker.insertAll(newJournalEntry);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_investigator:
                    nagivateToActivity(InvestigatorActivity.class);
                    return true;
                case R.id.navigation_journal:
                    nagivateToActivity(JournalActivity.class);
                    return true;
                case R.id.navigation_inventory:
                    nagivateToActivity(InventoryActivity.class);
                    return true;
                case R.id.navigation_settings:
                    nagivateToActivity(SettingsActivity.class);
                    return true;
            }
            return false;
        }
    };

    public void nagivateToActivity(Class targetActivity) {
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent);
    }

    private void playMediaSound(String filename)
    {
        int resID=getResources().getIdentifier(filename, "raw", getPackageName());

        MediaPlayer mediaPlayer=MediaPlayer.create(this,resID);
        mediaPlayer.start();
    }
}
