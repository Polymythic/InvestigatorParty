package com.example.investigatorparty;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by Steve on 2/21/2018.
 */

@Entity
        (foreignKeys = {
                @ForeignKey(
                        entity = RawCharacterSkillModel.class,
                        parentColumns = "rawSkillId",
                        childColumns = "skillRequired",
                        onDelete = CASCADE),
                @ForeignKey(
                        entity = GameEventModel.class,
                        parentColumns = "gameEventId",
                        childColumns = "eventSourceId",
                        onDelete = CASCADE),
                @ForeignKey(
                        entity = GameItemModel.class,
                        parentColumns = "itemId",
                        childColumns = "itemIdRequiredToEnable",
                        onDelete = CASCADE),
                @ForeignKey(
                        entity = GameEventModel.class,
                        parentColumns = "gameEventId",
                        childColumns = "eventIdIfSkillCheckPass",
                        onDelete = CASCADE),
                @ForeignKey(
                        entity = GameEventModel.class,
                        parentColumns = "gameEventId",
                        childColumns = "eventIdIfSkillCheckFail",
                        onDelete = CASCADE)})
public class GameEventTransitionModel {

    @PrimaryKey(autoGenerate = true)
    private int gameEventTransitionId;
    private int eventSourceId;              // Where does this transition originate from?
    private int itemIdRequiredToEnable;     // Is an item required to show a transition (example: key needed before interacting with a door)

    private int skillRequired;              //  Skill required to for transition
    private int skillDifficultyModifier;    // Modifier on difficulty of check (example: -20%)
    private int skillCheckDuration;         // What is the duration of the skill check?

    private int eventIdIfSkillCheckPass;    // Which event to transition to if check is passed
    private int eventIdIfSkillCheckFail;    // Which event to transition to if check is failed

    public int getGameEventTransitionId() {
        return gameEventTransitionId;
    }

    public void setGameEventTransitionId(int gameEventTransitionId) {
        this.gameEventTransitionId = gameEventTransitionId;
    }

    public int getEventSourceId() {
        return eventSourceId;
    }
    public void setEventSourceId(int eventSourceId) {
        this.eventSourceId = eventSourceId;
    }

    public int getItemIdRequiredToEnable() {
        return itemIdRequiredToEnable;
    }
    public void setItemIdRequiredToEnable(int itemIdRequiredToEnable) {
        this.itemIdRequiredToEnable = itemIdRequiredToEnable;
    }

    public int getSkillRequired() {
        return skillRequired;
    }
    public void setSkillRequired(int skillRequired) {
        this.skillRequired = skillRequired;
    }

    public int getSkillDifficultyModifier() {
        return skillDifficultyModifier;
    }
    public void setSkillDifficultyModifier(int skillDifficultyModifier) {
        this.skillDifficultyModifier = skillDifficultyModifier;
    }

    public int getSkillCheckDuration() {
        return skillCheckDuration;
    }
    public void setSkillCheckDuration(int skillCheckDuration) {
        this.skillCheckDuration = skillCheckDuration;
    }

    public int getEventIdIfSkillCheckPass() {
        return eventIdIfSkillCheckPass;
    }
    public void setEventIdIfSkillCheckPass(int eventIdIfSkillCheckPass) {
        this.eventIdIfSkillCheckPass = eventIdIfSkillCheckPass;
    }

    public int getEventIdIfSkillCheckFail() {
        return eventIdIfSkillCheckFail;
    }
    public void setEventIdIfSkillCheckFail(int eventIdIfSkillCheckFail) {
        this.eventIdIfSkillCheckFail = eventIdIfSkillCheckFail;
    }
}
