package com.example.investigatorparty;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * CharacterModel Statistic
 * This is a character statistic with a score

 * Entity: Represents a table within the database.
 *
 * Get / Set field values
 * Created by Steve on 1/27/2018.
 */
@Entity
        (foreignKeys = {
        @ForeignKey(
                entity = RawCharacterStatModel.class,
                parentColumns = "rawStatId",
                childColumns = "rawStatId",
                onDelete = CASCADE),
        @ForeignKey(
                entity = CharacterModel.class,
                parentColumns = "characterId",
                childColumns = "characterId",
                onDelete = CASCADE)})
public class CharacterStatModel {

    @PrimaryKey(autoGenerate = true)
    private int characterStatId;
    private int characterId;
    private int rawStatId;
    private int characterStatValue;

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public int getRawStatId() {
        return rawStatId;
    }

    public void setRawStatId(int rawStatId) {
        this.rawStatId = rawStatId;
    }

    public int getCharacterStatId() {
        return characterStatId;
    }

    public void setCharacterStatId(int characterStatId) {
        this.characterStatId = characterStatId;
    }

    public int getCharacterStatValue() {
        return characterStatValue;
    }

    public void setCharacterStatValue(int characterStatValue) {
        this.characterStatValue = characterStatValue;
    }
}