package com.digitalsanctuary.habittracker.data.local.dao
import androidx.room.*
import com.digitalsanctuary.habittracker.data.local.entity.JournalEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {
    @Query("SELECT * FROM journal_entries ORDER BY createdAt DESC")
    fun getAllEntries(): Flow<List<JournalEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: JournalEntry): Long

    @Delete
    suspend fun deleteEntry(entry: JournalEntry)
}
