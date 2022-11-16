package com.example.investigatorparty;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.PrimaryKey;


/**
 * Raw CharacterModel Statistic
 * This is a character statistic from the game (examples: Strength, Dexterity, etc)

 * Entity: Represents a table within the database.
 *
 * Get / Set field values
 * Created by Steve on 1/27/2018.
 */

@Entity
public class RawCharacterStatModel {

    @PrimaryKey(autoGenerate = true)
    private int rawStatId;
    private String rawStatName;
    private String rawStatDescription;

    public RawCharacterStatModel(String rawStatName)
    {
        this.rawStatName = rawStatName;
    }

    public int getRawStatId() {
        return rawStatId;
    }

    public void setRawStatId(int rawStatId) {
        this.rawStatId = rawStatId;
    }

    public String getRawStatName() {
        return rawStatName;
    }

    public void setRawStatName(String rawStatName) {
        this.rawStatName = rawStatName;
    }

    public String getRawStatDescription() {
        return rawStatDescription;
    }

    public void setRawStatDescription(String rawStatDescription) {
        this.rawStatDescription = rawStatDescription;
    }
}
