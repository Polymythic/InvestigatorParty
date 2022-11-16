package com.example.investigatorparty;

/**
 * Created by Steve on 2/21/2018.
 */

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = {
        @ForeignKey(
                entity = CharacterModel.class,
                parentColumns = "characterId",
                childColumns = "characterId",
                onDelete = CASCADE),
        @ForeignKey(
                entity = GameEventModel.class,
                parentColumns = "gameEventId",
                childColumns = "gameEventId",
                onDelete = CASCADE)})

public class GameEventTrackerModel {
    @PrimaryKey(autoGenerate = true)
    private int gameEventTrackerEntryId;

    private int characterId;
    private int gameEventId;

    public int getGameEventTrackerEntryId() {
        return gameEventTrackerEntryId;
    }

    public void setGameEventTrackerEntryId(int gameEventTrackerEntryId) {
        this.gameEventTrackerEntryId = gameEventTrackerEntryId;
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public int getGameEventId() {
        return gameEventId;
    }

    public void setGameEventId(int gameEventId) {
        this.gameEventId = gameEventId;
    }
}
