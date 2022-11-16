package com.example.investigatorparty;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class JournalActivity extends AppCompatActivity {

    // Get an instance of the database
    private AppDatabase db;
    private InvestigationDatabaseService databaseService;
    private int selectedCharacterId = SettingsActivity.DEFAULT_SELECTED_CHARACTER_ID;

    private ServiceConnection databaseServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            InvestigationDatabaseService.DatabaseServiceBinder databaseBinder = (InvestigationDatabaseService.DatabaseServiceBinder) service;
            databaseService = databaseBinder.getService();
            Log.i("JournalActivity", "onServiceConnected()");
            loadResourcePreferences();

            LinearLayout journalLayout = (LinearLayout) findViewById(R.id.journalLayout);

            // Update the display now that the database is up
            List<JournalTrackerModel> allJournalEntries = databaseService.getDb().journalTrackerDao().getAllJournalEntriesForCharacter(selectedCharacterId);

            // Clear the textView
            TextView newJournalTextView = new TextView(getApplicationContext());
            newJournalTextView.setText("");
            journalLayout.addView(newJournalTextView);

            // Add text for each journal entry
            for (JournalTrackerModel journalEntry : allJournalEntries)
            {
                GameEventModel gameEvent = databaseService.getDb().gameEventDao().getEventByClueCode(journalEntry.gameEventId);

                newJournalTextView.append("\n\n");
                newJournalTextView.append(gameEvent.getEventName());
                newJournalTextView.append("\n\t" + gameEvent.getEventDescription());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_investigator:
                    nagivateToActivity(InvestigatorActivity.class);
                    return true;
                case R.id.navigation_clues:
                    nagivateToActivity(ClueActivity.class);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        Intent intent = new Intent(this, InvestigationDatabaseService.class);
        bindService(intent, databaseServiceConnection, Context.BIND_AUTO_CREATE);
        Log.i("DB", "Binding to database service...");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_journal);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    protected void loadResourcePreferences() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        selectedCharacterId = sharedPref.getInt(getString(R.string.preferenceSelectedInvestigator), SettingsActivity.DEFAULT_SELECTED_CHARACTER_ID);
    }


    public void nagivateToActivity(Class targetActivity)
    {
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent);
    }


}
