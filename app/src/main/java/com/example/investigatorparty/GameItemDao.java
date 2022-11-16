package com.example.investigatorparty;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Steve on 2/22/2018.
 */


@Dao
public interface GameItemDao {
    @Query("SELECT * FROM GameItemModel")
    List<GameItemModel> getAllGameItems();

    @Query("SELECT * FROM GameItemModel WHERE skillIdModifiedByItem=:skillId")
    List<GameItemModel> getItemsThatModifySkill(int skillId);

    @Query("SELECT * FROM GameItemModel LEFT JOIN InventoryTrackerModel ON InventoryTrackerModel.itemId = GameItemModel.itemId WHERE InventoryTrackerModel.characterId=:characterId AND skillIdModifiedByItem=:skillId")
    List<GameItemModel> getItemsForCharacterThatModifySkill(int skillId, int characterId);

    @Query("SELECT * FROM GameItemModel WHERE itemId=:id")
    GameItemModel getById(int id);

    @Query("DELETE FROM GameItemModel")
    public void dropTable();

    @Insert
    void insertAll(GameItemModel... gameItemModels);

    @Delete
    void delete(GameItemModel gameItemModels);
}
