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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    // @TODO - Add a quit mission (refresh the database by recopying it)
    public final static int DEFAULT_SELECTED_CHARACTER_ID = 1;

    // Get an instance of the database
    private AppDatabase db;
    private InvestigationDatabaseService databaseService;
    private int characterIdSelected = 1;


    private ServiceConnection databaseServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            InvestigationDatabaseService.DatabaseServiceBinder databaseBinder = (InvestigationDatabaseService.DatabaseServiceBinder) service;
            databaseService = databaseBinder.getService();
            Log.i("SettingsActivity", "onServiceConnected()");

            loadResourcePreferences();

            // Get the list of characters
            final List<String> characterList = new ArrayList<String>();
            List<CharacterModel> characters = databaseService.getDb().characterDao().getAllCharacters();
            for (CharacterModel character : characters) {
                characterList.add(character.getName());
            }

            // Populate the characters in a spinner
            final Spinner characterSelectionSpinner = (Spinner) findViewById(R.id.characterSelectSpinner);
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, characterList);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            characterSelectionSpinner.setAdapter(spinnerAdapter);
            // Set the selection based on character ID
            // TODO This may cause a problem when the ids are by name or other.
            characterSelectionSpinner.setSelection(characterIdSelected-1);

            characterSelectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View arg1, int position, long id) {
                    String selection = parent.getItemAtPosition(position).toString();
                    CharacterModel selectedCharacter = databaseService.getDb().characterDao().findByName(selection);
                    characterIdSelected = selectedCharacter.getCharacterId();
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent intent = new Intent(this, InvestigationDatabaseService.class);
        bindService(intent, databaseServiceConnection, Context.BIND_AUTO_CREATE);
        Log.i("DB", "Binding to database service...");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_settings);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        saveResourcePreferences();
    }

    public void playMusic(View view)
    {
        Intent musicIntent = new Intent(this, MusicService.class);
        musicIntent.putExtra("startMusic", true);
        startService(musicIntent);
    }


    public void clearJournal(View view)
    {
        databaseService.getDb().journalTrackerDao().dropAllTableData();
    }

    public void listStuff(View view) {
        // List the database path
        TextView databaseTextView = null;
        databaseTextView = (TextView) findViewById(R.id.databaseTextView);
        databaseTextView.setText("Database Path: " + databaseService.getDatabasePath("investigatorparty_db"));

        // List characters
        databaseTextView.append("\nCharacters:");
        List<CharacterModel> characterList = databaseService.getDb().characterDao().getAllCharacters();
        for (CharacterModel character : characterList)
        {
            databaseTextView.append("\n" + character.getName());
        }
    }

    protected void saveResourcePreferences()
    {
        // Write to a shared preference file
        SharedPreferences sharedPreferenceFile = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferenceFile.edit();

        editor.putInt(getString(R.string.preferenceSelectedInvestigator), characterIdSelected);
        Log.d("PREFERENCES", getString(R.string.preferenceSelectedInvestigator) + "  Value: " + Integer.toString(characterIdSelected));
        editor.commit();
    }


    protected void loadResourcePreferences() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        characterIdSelected = sharedPref.getInt(getString(R.string.preferenceSelectedInvestigator), DEFAULT_SELECTED_CHARACTER_ID);
    }

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
                case R.id.navigation_journal:
                    nagivateToActivity(JournalActivity.class);
                    return true;
            }
            return false;
        }
    };

    public void nagivateToActivity(Class targetActivity)
    {
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent);
    }


}
