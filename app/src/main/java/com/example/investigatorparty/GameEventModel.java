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
                        entity = GameItemModel.class,
                        parentColumns = "itemId",
                        childColumns = "itemGrantedForEvent",
                        onDelete = CASCADE),
                @ForeignKey(
                        entity = GameItemModel.class,
                        parentColumns = "itemId",
                        childColumns = "itemToPreventSanityLoss",
                        onDelete = CASCADE),
                @ForeignKey(
                        entity = GameItemModel.class,
                        parentColumns = "itemId",
                        childColumns = "itemToPreventHealthLoss",
                        onDelete = CASCADE)})
public class GameEventModel {

    @PrimaryKey(autoGenerate = false)
    private int gameEventId;

    private boolean isEventTracked = false;             // Is this event "marked" as completed to not show again
    private String eventName = "No name";               // Event Name (title)
    private String eventDescription = "No description"; // Longer description of event

    // Foreign Key
    private int itemGrantedForEvent = -1;                   // Item added to inventory at this event
    private int sanityLossForEvent = 0;                     // Sanity lost if event is revealed
    private int healthLossForEvent = 0;                     // Health lost if event is revealed

    // Foreign Key
    private int gameEventIdToPreventSanityLoss = -1;         // Event that, if in Event Tracker, avoids loss
    // Foreign Key
    private int itemToPreventSanityLoss = -1;                // Item that, if in inventory, avoids loss
    // Foreign Key
    private int gameEventIdToPreventHealthLoss = -1;         // Event that, if in Event Tracker, avoids loss
    // Foreign Key
    private int itemToPreventHealthLoss = -1;                // Item that, if in inventory, avoids loss


    public int getGameEventId() {
        return gameEventId;
    }
    public void setGameEventId(int gameEventId) {
        this.gameEventId = gameEventId;
    }

    public boolean isEventTracked() {
        return isEventTracked;
    }
    public void setEventTracked(boolean eventTracked) {
        isEventTracked = eventTracked;
    }

    public String getEventName() {
        return eventName;
    }
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }
    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public int getItemGrantedForEvent() {
        return itemGrantedForEvent;
    }
    public void setItemGrantedForEvent(int itemGrantedForEvent) {
        this.itemGrantedForEvent = itemGrantedForEvent;
    }


    public int getSanityLossForEvent() {
        return sanityLossForEvent;
    }
    public void setSanityLossForEvent(int sanityLossForEvent) {
        this.sanityLossForEvent = sanityLossForEvent;
    }

    public int getHealthLossForEvent() {
        return healthLossForEvent;
    }
    public void setHealthLossForEvent(int healthLossForEvent) {
        this.healthLossForEvent = healthLossForEvent;
    }

    public int getGameEventIdToPreventSanityLoss() {
        return gameEventIdToPreventSanityLoss;
    }

    public void setGameEventIdToPreventSanityLoss(int gameEventIdToPreventSanityLoss) {
        this.gameEventIdToPreventSanityLoss = gameEventIdToPreventSanityLoss;
    }

    public int getItemToPreventSanityLoss() {
        return itemToPreventSanityLoss;
    }

    public void setItemToPreventSanityLoss(int itemToPreventSanityLoss) {
        this.itemToPreventSanityLoss = itemToPreventSanityLoss;
    }

    public int getGameEventIdToPreventHealthLoss() {
        return gameEventIdToPreventHealthLoss;
    }

    public void setGameEventIdToPreventHealthLoss(int gameEventIdToPreventHealthLoss) {
        this.gameEventIdToPreventHealthLoss = gameEventIdToPreventHealthLoss;
    }

    public int getItemToPreventHealthLoss() {
        return itemToPreventHealthLoss;
    }

    public void setItemToPreventHealthLoss(int itemToPreventHealthLoss) {
        this.itemToPreventHealthLoss = itemToPreventHealthLoss;
    }
}