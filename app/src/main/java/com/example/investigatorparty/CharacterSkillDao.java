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
public interface CharacterSkillDao {
    @Query("SELECT * FROM CharacterSkillModel")
    List<CharacterSkillModel> getAllCharacterSkills();

    @Query("SELECT * FROM CharacterSkillModel WHERE characterId=:characterId")
    List<CharacterSkillModel> getSkillsForCharacter(final int characterId);

    @Query("SELECT * FROM CharacterSkillModel WHERE CharacterSkillModel.characterId=:charId AND CharacterSkillModel.rawSkillId=:skillId LIMIT 1")
    CharacterSkillModel getSkillForCharacter(final int charId, final int skillId);

    @Query("DELETE FROM CharacterSkillModel")
    public void dropTable();

    @Insert
    void insertAll(CharacterSkillModel... characterSkillModels);

    @Delete
    void delete(CharacterSkillModel characterSkillModel);
}