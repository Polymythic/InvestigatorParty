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
                entity = GameItemModel.class,
                parentColumns = "itemId",
                childColumns = "itemId",
                onDelete = CASCADE)})
public class InventoryTrackerModel {

    @PrimaryKey(autoGenerate = true)
    private int inventoryEntryId;

    // Foreign
    int characterId;
    // Foreign
    int itemId;

    public int getInventoryEntryId() {
        return inventoryEntryId;
    }

    public void setInventoryEntryId(int inventoryEntryId) {
        this.inventoryEntryId = inventoryEntryId;
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
}
