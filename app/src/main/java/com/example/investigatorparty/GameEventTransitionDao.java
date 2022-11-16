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
public interface GameEventTransitionDao {
    @Query("SELECT * FROM GameEventTransitionModel")
    List<GameEventTransitionModel> getAllGameTransitions();

    @Query("SELECT * FROM GameEventTransitionModel WHERE eventSourceId=:clueCode")
    List<GameEventTransitionModel> getAllTransitionsForEvent(final int clueCode);

    @Query("SELECT * FROM GameEventTransitionModel WHERE gameEventTransitionId=:id")
    GameEventTransitionModel getGameEventTransitionById(final int id);

    @Insert
    void insertAll(GameEventTransitionModel... gameEventTransitionModel);

    @Delete
    void delete(GameEventTransitionModel gameEventTransitionModel);
}
