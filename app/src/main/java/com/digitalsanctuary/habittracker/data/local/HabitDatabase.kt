package com.digitalsanctuary.habittracker.data.local

import android.content.Context
import androidx.room.*
import com.digitalsanctuary.habittracker.data.local.dao.CompletionDao
import com.digitalsanctuary.habittracker.data.local.dao.HabitDao
import com.digitalsanctuary.habittracker.data.local.dao.JournalDao
import com.digitalsanctuary.habittracker.data.local.entity.CompletionEntity
import com.digitalsanctuary.habittracker.data.local.entity.HabitEntity
import com.digitalsanctuary.habittracker.data.local.entity.JournalEntry

@Database(
    entities = [HabitEntity::class, JournalEntry::class, CompletionEntity::class],
    version = 5,
    exportSchema = false
)
abstract class HabitDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun journalDao(): JournalDao
    abstract fun completionDao(): CompletionDao

    companion object {
        @Volatile private var INSTANCE: HabitDatabase? = null
        fun getDatabase(context: Context): HabitDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context, HabitDatabase::class.java, "habit_tracker_db")
                    .fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
    }
}
