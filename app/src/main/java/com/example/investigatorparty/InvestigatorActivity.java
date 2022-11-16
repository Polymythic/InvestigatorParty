package com.example.investigatorparty;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.content.ServiceConnection;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.investigatorparty.InvestigationDatabaseService.DatabaseServiceBinder;

import java.util.List;


public class InvestigatorActivity extends AppCompatActivity {

    // Get an instance of the database
    private AppDatabase db;
    private InvestigationDatabaseService databaseService;
    private CharacterModel selectedCharacterModel;
    private int selectedCharacterId = SettingsActivity.DEFAULT_SELECTED_CHARACTER_ID;

    private ServiceConnection databaseServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DatabaseServiceBinder databaseBinder = (DatabaseServiceBinder) service;
            databaseService = databaseBinder.getService();
            Log.i("InvestigatorActivity", "onServiceConnected()");

            loadResourcePreferences();

            // Update the display now that the database is up
            selectedCharacterModel = databaseService.getDb().characterDao().findById(selectedCharacterId);
            //TODO schema should specify the artwork
            int resourceSpecified;
            switch (selectedCharacterId)
            {
                case 1:
                    resourceSpecified = R.mipmap.ic_male1;
                    break;
                case 2:
                    resourceSpecified = R.mipmap.ic_female1;
                    break;
                case 3:
                    resourceSpecified = R.mipmap.ic_male2;
                    break;
                default:
                    resourceSpecified = R.mipmap.ic_male3;
                    break;

            }
            ImageView characterImage = (ImageView) findViewById(R.id.characterImage);
            characterImage.setImageResource(resourceSpecified);

            updateCharacterInformation();
            updateSkillsTableView();
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
                case R.id.navigation_clues:
                    nagivateToActivity(ClueActivity.class);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investigator);

        // Creation of the service.  All other activities should simply bind.
        Intent databaseIntent = new Intent(this, InvestigationDatabaseService.class);
        startService(databaseIntent);

        // Add the navigation
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_investigator);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Bind to database service
        bindService(databaseIntent, databaseServiceConnection, Context.BIND_AUTO_CREATE);
        Log.i("DB", "Binding to database service...");

        // TODO Kick off the music service automatically?
//        Intent musicIntent = new Intent(this, MusicService.class);
//        musicIntent.putExtra("startMusic", true);
//        startService(musicIntent);


    }

    public void nagivateToActivity(Class targetActivity)
    {
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Intent intent = new Intent(this, InvestigationDatabaseService.class);
        startService(intent);
        bindService(intent, databaseServiceConnection, Context.BIND_AUTO_CREATE);
        Log.v("InvestigatorActivity", "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadResourcePreferences();
    }


    private void updateCharacterInformation()
    {
        TextView nameView = (TextView) findViewById(R.id.nameValue);
        nameView.setText(selectedCharacterModel.getName());

        // @TODO profession or occupation?
        TextView occupationView = (TextView) findViewById(R.id.occupationValue);
        occupationView.setText(selectedCharacterModel.getProfession());

        TextView birthplaceView = (TextView) findViewById(R.id.birthplaceValue);
        birthplaceView.setText(selectedCharacterModel.getBirthplace());

        TextView ageView = (TextView) findViewById(R.id.ageValue);
        ageView.setText(Integer.toString(selectedCharacterModel.getAge()));

        TextView hitpointsView = (TextView) findViewById(R.id.hitpointValue);
        hitpointsView.setText(Integer.toString(selectedCharacterModel.getHitPoints()));

        TextView sanityView = (TextView) findViewById(R.id.sanityValue);
        sanityView.setText(Integer.toString(selectedCharacterModel.getSanityPoints()));
    }

    private void updateSkillsTableView()
    {
        if (selectedCharacterModel != null)
        {
            // Get the skill list for the selected character
            List<CharacterSkillModel> allSkills = databaseService.getDb().characterSkillDao().getSkillsForCharacter(selectedCharacterModel.getCharacterId());

            for (CharacterSkillModel skill : allSkills) {
                TableLayout skillsTableLayout = (TableLayout) findViewById(R.id.skillTableLayout);
                TableRow newRow = new TableRow(this);

                // Lookup the name of the rawCharacterSkill by its ID
                RawCharacterSkillModel rawSkill = databaseService.getDb().rawCharacterSkillDao().findById(skill.getRawSkillId());

                // Set the skill text display
                TextView skillLabelTextView = new TextView(this);
                skillLabelTextView.setText(rawSkill.getRawSkillName());
                skillLabelTextView.setTextSize(15);
                skillLabelTextView.setGravity(Gravity.LEFT);

                // Set the value
                TextView skillValueTextView = new TextView(this);
                skillValueTextView.setText(Integer.toString(skill.getSkillValue()) + "%");
                skillValueTextView.setTextSize(15);
                skillValueTextView.setGravity(Gravity.RIGHT);

                newRow.addView(skillLabelTextView);
                newRow.addView(skillValueTextView);
                skillsTableLayout.addView(newRow);
            }
        }
    }

    protected void loadResourcePreferences() {
        // Read from a shared preference file
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        selectedCharacterId = sharedPref.getInt(getString(R.string.preferenceSelectedInvestigator), 1);
        Log.d("PREFERENCES", "InvestigatorActivity - Loading: " + getString(R.string.preferenceSelectedInvestigator) + " Value: "+ Integer.toString(selectedCharacterId));
    }

}
