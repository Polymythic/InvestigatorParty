package com.example.investigatorparty;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * // https://android.jlelse.eu/android-architecture-components-room-relationships-bf473510c14a
 * https://www.bignerdranch.com/blog/room-data-storage-for-everyone/
 * https://android.jlelse.eu/android-architecture-components-room-livedata-and-viewmodel-fca5da39e26b
 * https://medium.com/@ajaysaini.official/building-database-with-room-persistence-library-ecf7d0b8f3e9
 * https://android.jlelse.eu/android-architecture-components-room-relationships-bf473510c14a
 * Created by Steve on 1/27/2018.
 */

@Database(entities =
        {       CharacterModel.class,
                CharacterSkillModel.class,
                CharacterStatModel.class,
                GameEventModel.class,
                GameEventTrackerModel.class,
                GameEventTransitionModel.class,
                GameItemModel.class,
                InventoryTrackerModel.class,
                JournalTrackerModel.class,
                RawCharacterStatModel.class,
                RawCharacterSkillModel.class
        },  version = 1)
    abstract class AppDatabase extends RoomDatabase {

        // Accessors to the Data Access Objects
        abstract public CharacterDao characterDao();
        abstract public CharacterSkillDao characterSkillDao();
        abstract public CharacterStatDao characterStatDao();
        abstract public GameEventDao gameEventDao();
        abstract public GameEventTrackerDao gameEventTrackerDao();
        abstract public GameEventTransitionDao gameEventTransitionDao();
        abstract public GameItemDao gameItemDao();
        abstract public InventoryTrackerDao inventoryTrackerDao();
        abstract public JournalTrackerDao journalTrackerDao();
        abstract public RawCharacterSkillDao rawCharacterSkillDao();
        abstract public RawCharacterStatDao rawCharacterStatDao();

}
