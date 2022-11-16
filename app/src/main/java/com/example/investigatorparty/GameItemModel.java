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
                entity = RawCharacterSkillModel.class,
                parentColumns = "rawSkillId",
                childColumns = "skillIdModifiedByItem",
                onDelete = CASCADE)})
public class GameItemModel {

    @PrimaryKey(autoGenerate = true)
    private int itemId;

    private String description;
    // Foreign
    private int skillIdModifiedByItem;      // Which skill is modified by the item
    private int skillCheckModifier;         // Modifier applied with item (+ or -)

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSkillIdModifiedByItem() {
        return skillIdModifiedByItem;
    }

    public void setSkillIdModifiedByItem(int skillIdModifiedByItem) {
        this.skillIdModifiedByItem = skillIdModifiedByItem;
    }

    public int getSkillCheckModifier() {
        return skillCheckModifier;
    }

    public void setSkillCheckModifier(int skillCheckModifier) {
        this.skillCheckModifier = skillCheckModifier;
    }
}
