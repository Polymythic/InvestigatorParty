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
public interface RawCharacterSkillDao {
    @Query("SELECT * FROM RawCharacterSkillModel")
    List<RawCharacterSkillModel> getAllRawCharacterSkills();

    @Query("SELECT * FROM rawcharacterskillmodel WHERE rawSkillId=:rawSkillName LIMIT 1")
    RawCharacterSkillModel findByName(String rawSkillName);

    @Query("SELECT * FROM rawcharacterskillmodel WHERE rawSkillId LIKE :rawSkillId LIMIT 1")
    RawCharacterSkillModel findById(int rawSkillId);

    @Query("DELETE FROM RawCharacterSkillModel")
    public void dropTable();

    @Insert
    void insertAll(RawCharacterSkillModel... rawCharacterSkillModels);

    @Delete
    void delete(RawCharacterSkillModel rawCharacterSkillsModel);
}