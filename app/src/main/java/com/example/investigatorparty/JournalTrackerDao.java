package com.example.investigatorparty;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Steve on 2/22/2018.
 */

@Dao
public interface JournalTrackerDao {

    @Query("SELECT * FROM journaltrackermodel")
    List<JournalTrackerModel> getAllJournalEntries();

    @Query("SELECT * FROM journaltrackermodel WHERE characterId=:characterId")
    List<JournalTrackerModel> getAllJournalEntriesForCharacter(int characterId);

    @Query("SELECT * FROM JournalTrackerModel WHERE characterId=:searchCharId AND gameEventId=:gameEventId")
    List<JournalTrackerModel> getEventsForCharacterByGameId(int searchCharId, int gameEventId);

    @Query("DELETE FROM journaltrackermodel")
    public void dropAllTableData();

    @Insert
    void insertAll(JournalTrackerModel... journalTrackerModels);

    @Delete
    void delete(JournalTrackerModel journalTrackerModels);
}

