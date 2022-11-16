package com.example.investigatorparty;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.PrimaryKey;


/**
 * * Raw CharacterModel Skill
 * This is a character statistic from the game (examples: Accounting, Anthropology, etc)
 * Entity: Represents a table within the database.
 *
 * Get / Set field values
 * Created by Steve on 1/27/2018.
*/

@Entity
public class RawCharacterSkillModel {

    @PrimaryKey(autoGenerate = true)
    private int rawSkillId;

    private String rawSkillName;
    private String rawSkillDescription;
    private int rawSkillMinimumScore;

    public RawCharacterSkillModel(String rawSkillName)
    {
        this.rawSkillName = rawSkillName;
    }

    public int getRawSkillId() {
         return rawSkillId;
     }

    public void setRawSkillId(int rawSkillId) {
        this.rawSkillId = rawSkillId;
    }

    public String getRawSkillName() {
        return rawSkillName;
    }

    public void setRawSkillName(String rawSkillName) {
        this.rawSkillName = rawSkillName;
    }

    public String getRawSkillDescription() {
        return rawSkillDescription;
    }

    public void setRawSkillDescription(String rawSkillDescription) {
        this.rawSkillDescription = rawSkillDescription;
    }

    public int getRawSkillMinimumScore() {
        return rawSkillMinimumScore;
    }

    public void setRawSkillMinimumScore(int rawSkillMinimumScore) {
        this.rawSkillMinimumScore = rawSkillMinimumScore;
    }
}