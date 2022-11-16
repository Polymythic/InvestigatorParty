package com.example.investigatorparty;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * DAO: Contains the methods used for accessing the database.
 * Get Entities from the db
 * Persist changes back to the db
 * Created by Steve on 1/27/2018.
 */

@Dao
public interface CharacterStatDao {
    @Query("SELECT * FROM CharacterStatModel")
    List<CharacterStatModel> getAllCharacterStats();

    @Query("SELECT * FROM characterstatmodel WHERE characterId=:characterId")
    List<CharacterStatModel> getStatsForCharacter(final int characterId);

//    @Query("SELECT * FROM user WHERE cid IN (:characterIds)")
//    List<CharacterModel> loadAllCharactersByIds(int[] characterIds);

//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND "
//            + "last_name LIKE :last LIMIT 1")
//    User findByName(String first, String last);

    @Insert
    void insertAll(CharacterStatModel... characterStatModels);

    @Delete
    void delete(CharacterStatModel characterStatsModel);
}