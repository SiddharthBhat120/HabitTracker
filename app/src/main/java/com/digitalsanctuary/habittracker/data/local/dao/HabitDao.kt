package com.digitalsanctuary.habittracker.data.local.dao
import androidx.room.*
import com.digitalsanctuary.habittracker.data.local.entity.HabitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits ORDER BY createdDate ASC")
    fun getAllHabits(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habits WHERE id = :id")
    fun getHabitById(id: Long): Flow<HabitEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: HabitEntity): Long

    @Update
    suspend fun updateHabit(habit: HabitEntity)

    @Delete
    suspend fun deleteHabit(habit: HabitEntity)

    @Query("SELECT COUNT(*) FROM habits WHERE isCompletedToday = 1")
    fun getCompletedTodayCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM habits")
    fun getTotalHabitCount(): Flow<Int>
}
