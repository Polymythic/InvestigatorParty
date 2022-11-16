package com.example.investigatorparty;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
     * CharacterSkillModel
     * This is a character skill with a score

     * Entity: Represents a table within the database.
     *
     * Get / Set field values
     * Created by Steve on 1/27/2018.
     */

    // Entity - Other table to form relation to
    // ParentColumns - key column from other table
    // ChildColumns - What should we call this relationship in this database?
        // onDelete - CASCADE - if this row is deleted delete if from all repositories

@Entity(foreignKeys = {
        @ForeignKey(
            entity = RawCharacterSkillModel.class,
            parentColumns = "rawSkillId",
            childColumns = "rawSkillId",
            onDelete = CASCADE),
        @ForeignKey(
            entity = CharacterModel.class,
            parentColumns = "characterId",
            childColumns = "characterId",
            onDelete = CASCADE)})
public class CharacterSkillModel {

    @PrimaryKey(autoGenerate = true)
    private int characterSkillId;
    private int characterId;
    private int rawSkillId;
    private int skillValue;

    public int getCharacterSkillId() {
        return characterSkillId;
    }
    public void setCharacterSkillId(int characterSkillId) {
        this.characterSkillId = characterSkillId;
    }

    public int getCharacterId() {
        return characterId;
    }
    public void setCharacterId(int characterId) { this.characterId = characterId; }

    public int getRawSkillId() {
        return rawSkillId;
    }
    public void setRawSkillId(int rawSkillId) {
        this.rawSkillId = rawSkillId;
    }

    public int getSkillValue() {
         return skillValue;
    }
    public void setSkillValue(int skillValue) {
         this.skillValue = skillValue;
    }
}