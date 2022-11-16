package com.example.investigatorparty;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;


import java.util.List;

/**
 * DAO: Contains the methods used for accessing the database.
 * Get Entities from the db
 * Persist changes back to the db
 * Created by Steve on 1/27/2018.
 */

@Dao
public interface InventoryTrackerDao {
    @Query("SELECT * FROM InventoryTrackerModel")
    List<InventoryTrackerModel> getAllInventoryEntries();

    @Query("SELECT * FROM InventoryTrackerModel WHERE characterId=:id")
    List<InventoryTrackerModel> getItemsForCharacter(final int id);

    @Query("SELECT * FROM InventoryTrackerModel WHERE characterId=:searchCharId AND itemId=:searchItemId")
    List<InventoryTrackerModel> itemAssignedToCharacter(int searchCharId, int searchItemId);

    @Insert
    void insertAll(InventoryTrackerModel... inventoryTrackerModels);

    @Delete
    void delete(InventoryTrackerModel inventoryTrackerModel);
}
