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

public class InventoryActivity extends AppCompatActivity {

    // Get an instance of the database
    private AppDatabase db;
    private InvestigationDatabaseService databaseService;
    private int selectedCharacterId;

    private ServiceConnection databaseServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            InvestigationDatabaseService.DatabaseServiceBinder databaseBinder = (InvestigationDatabaseService.DatabaseServiceBinder) service;
            databaseService = databaseBinder.getService();

            loadResourcePreferences();

            // Clear the list
            LinearLayout inventoryListLayout = (LinearLayout) findViewById(R.id.inventoryListLayout);
            inventoryListLayout.removeAllViews();

            List<InventoryTrackerModel> inventory = databaseService.getDb().inventoryTrackerDao().getItemsForCharacter(selectedCharacterId);
            if (inventory.isEmpty()) {
                TextView newItemToList = new TextView(getApplicationContext());
                newItemToList.setText("You are carrying nothing.");
                inventoryListLayout.addView(newItemToList);
            }
            else {
                // Display each item in the character's inventory
                for (InventoryTrackerModel itemEntry : inventory) {
                    String itemName = databaseService.getDb().gameItemDao().getById(itemEntry.getItemId()).getDescription();

                    TextView newItemToList = new TextView(getApplicationContext());
                    newItemToList.setText(itemName);
                    inventoryListLayout.addView(newItemToList);
                }
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
                case R.id.navigation_journal:
                    nagivateToActivity(JournalActivity.class);
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
        setContentView(R.layout.activity_inventory);

        Intent intent = new Intent(this, InvestigationDatabaseService.class);
        bindService(intent, databaseServiceConnection, Context.BIND_AUTO_CREATE);
        Log.i("DB", "Binding to database service...");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_inventory);
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
