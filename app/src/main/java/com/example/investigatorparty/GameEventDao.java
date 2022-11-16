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
public interface GameEventDao {
    @Query("SELECT * FROM GameEventModel")
    List<GameEventModel> getAllGameEvents();

    @Query("SELECT * FROM GameEventModel WHERE gameEventId=:clueCode")
    GameEventModel getEventByClueCode(final int clueCode);

    @Insert
    void insertAll(GameEventModel... gameEventModel);

    @Delete
    void delete(GameEventModel gameEventModel);
}
