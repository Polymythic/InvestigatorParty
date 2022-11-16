package com.example.investigatorparty;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by Steve on 2/21/2018.
 */

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
public class JournalTrackerModel {
    @PrimaryKey(autoGenerate = true)
    private int journalEntryId;

    // Foreign
    int characterId;
    // Foreign
    int gameEventId;

    public int getJournalEntryId() {
        return journalEntryId;
    }

    public void setJournalEntryId(int journalEntryId) {
        this.journalEntryId = journalEntryId;
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
