package com.example.investigatorparty;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Steve on 2/24/2018.
 */

@Dao
public interface GameEventTrackerDao {
    @Query("SELECT * FROM GameEventTrackerModel")
    List<GameEventTrackerModel> getAllGameTrackerEvents();

    @Query("SELECT * FROM GameEventTrackerModel WHERE characterId=:characterId")
    List<GameEventTrackerModel> getEventsForCharacter(final int characterId);

    // Get all entries from DATABASE A, joining the set to DATABASE B
    // ON the condition where a primary key is the same
    // WHERE the following is true
    @Query("SELECT * FROM GameEventModel INNER JOIN GameEventTrackerModel ON " +
            "GameEventModel.gameEventId=GameEventTrackerModel.gameEventId " +
            "WHERE GameEventTrackerModel.characterId=:characterId")
    List<GameEventModel> getAllEventsForCharacter(final int characterId);

    @Insert
    void insertAll(GameEventTrackerModel... gameEventTrackerModel);

    @Delete
    void delete(GameEventTrackerModel gameEventTrackerModel);
}
