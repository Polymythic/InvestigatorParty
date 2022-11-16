package com.example.investigatorparty;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;

import android.app.Service;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.List;

public class InvestigationDatabaseService extends Service {

    private AppDatabase db = null;
    private IBinder serviceBinder = new DatabaseServiceBinder();
    private static final String DB_NAME = "investigator.db";

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
//                database.execSQL("CREATE TABLE `Fruit` (`id` INTEGER, "
//                        + "`name` TEXT, PRIMARY KEY(`id`))");
        }
    };

    @Override
    public void onCreate()
    {
        // Copy the pre-made database to where Room expects/creates its database, and
        // add a migration so Room does not overrwite it and drop all data
        // https://stackoverflow.com/questions/44263891/how-to-use-room-persistence-library-with-pre-populated-database
        Log.i("DATABASE", "onCreate()");

        try {
            copyPrepopulatedDatabase(DB_NAME);
        }
        catch (Exception e)
        {
            Log.i("DATABASE", "Exception Error while trying to copy file");
            Log.i("DATABASE", e.toString());
        }

        // Get the instance of the database
        // TODO revert this mainThreadQuery permission once we make more progress (should be async using LiveData)
        // Adding a room migration: From documentation
        // https://developer.android.com/training/data-storage/room/migrating-db-versions
        // Caution: If you don't provide the necessary migrations, Room rebuilds the database instead,
        // which means you'll lose all of your data in the database.
        db = Room.databaseBuilder(getApplicationContext(),AppDatabase.class,DB_NAME)
                .allowMainThreadQueries()
//                .fallbackToDestructiveMigration()
                .addMigrations(MIGRATION_1_2)
                .build();
    }

    public InvestigationDatabaseService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.i("DATABASE", "Starting InvestigationDatabaseService service");
        return 1;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        Log.i("DATABASE", "InvestigationDatabaseService - Client is binding to database..");
        return serviceBinder;
    }

    public class DatabaseServiceBinder extends Binder {
        public InvestigationDatabaseService getService() {
            return InvestigationDatabaseService.this;
        }
    }

    public AppDatabase getDb()
    {
        return db;
    }

    private void copyPrepopulatedDatabase(String databaseName) throws IOException {


        InputStream pregenDatabase = getApplicationContext().getAssets().open("databases/investigator.db");
        Log.i("DATABASE", "Pregenerated Database: " + pregenDatabase.toString());
        if (pregenDatabase.available() >0)
        {
            Log.i("DATABASE", "Opened for reading and available");
        }

        final File databaseFile = getApplicationContext().getDatabasePath(DB_NAME);
        Log.i("DATABASE", "Database file expected to be used by Room: " + databaseFile.getAbsolutePath());


        if (!databaseFile.exists()) {
            Log.i("DATABASE", "The database file does not exist.");
            Log.i("DATABASE", "Copying pre-populated database: DESTINATION: " + databaseFile.getAbsolutePath());

            OutputStream myOutput = new FileOutputStream(databaseFile);
            byte[] buffer = new byte[1024];
            int length;

            while ((length = pregenDatabase.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            pregenDatabase.close();
            myOutput.flush();
            myOutput.close();
        }
        else
        {
            Log.i("DATABASE", "The database file already exists.  Did not copy from assets.");
            Log.i("DATABASE", "Copying pre-populated database: DESTINATION: " + databaseFile.getAbsolutePath());

            OutputStream myOutput = new FileOutputStream(databaseFile);
            byte[] buffer = new byte[1024];
            int length;

            while ((length = pregenDatabase.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            pregenDatabase.close();
            myOutput.flush();
            myOutput.close();

        }
    }
};


