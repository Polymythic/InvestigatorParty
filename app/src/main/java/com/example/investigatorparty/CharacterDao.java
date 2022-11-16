package com.example.investigatorparty;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * DAO: Contains the methods used for accessing the database.
 * Get Entities from the db
 * Persist changes back to the db
 * Created by Steve on 1/27/2018.
 */

@Dao
public interface CharacterDao {
    @Query("SELECT * FROM CharacterModel")
    List<CharacterModel> getAllCharacters();

    @Query("SELECT * FROM CharacterModel WHERE characterId=:id")
    CharacterModel findById(int id);

    @Query("SELECT * FROM CharacterModel WHERE name LIKE :characterName LIMIT 1")
    CharacterModel findByName(String characterName);

    @Query("DELETE FROM CharacterModel")
    public void dropTable();

    @Update
    void update(CharacterModel characterModel);

    @Insert
    void insertAll(CharacterModel... characterModels);

    @Delete
    void delete(CharacterModel characters);
}