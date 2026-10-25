package com.digitalsanctuary.habittracker.data.local.dao

import androidx.room.*
import com.digitalsanctuary.habittracker.data.local.entity.CompletionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CompletionDao {
    @Query("SELECT * FROM completions WHERE date = :date")
    fun getCompletionsByDate(date: String): Flow<List<CompletionEntity>>

    @Query("SELECT * FROM completions WHERE habitId = :habitId AND date = :date LIMIT 1")
    suspend fun getCompletion(habitId: Long, date: String): CompletionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompletion(completion: CompletionEntity)

    @Delete
    suspend fun deleteCompletion(completion: CompletionEntity)

    @Query("SELECT DISTINCT date FROM completions")
    fun getAllCompletedDates(): Flow<List<String>>
    
    @Query("SELECT COUNT(DISTINCT date) FROM completions")
    fun getTotalCompletedDaysCount(): Flow<Int>

    @Query("SELECT * FROM completions")
    fun getAllCompletions(): Flow<List<CompletionEntity>>
}
