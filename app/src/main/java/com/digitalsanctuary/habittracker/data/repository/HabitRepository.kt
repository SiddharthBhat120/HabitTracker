package com.digitalsanctuary.habittracker.data.repository

import com.digitalsanctuary.habittracker.data.local.dao.CompletionDao
import com.digitalsanctuary.habittracker.data.local.dao.HabitDao
import com.digitalsanctuary.habittracker.data.local.entity.CompletionEntity
import com.digitalsanctuary.habittracker.data.local.entity.HabitEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.*

class HabitRepository(
    private val habitDao: HabitDao,
    private val completionDao: CompletionDao
) {
    val allHabits: Flow<List<HabitEntity>> = habitDao.getAllHabits()
    val allCompletions: Flow<List<CompletionEntity>> = completionDao.getAllCompletions()
    val totalCompletedDaysCount: Flow<Int> = completionDao.getTotalCompletedDaysCount()

    fun getHabitById(id: Long): Flow<HabitEntity?> = habitDao.getHabitById(id)
    suspend fun insertHabit(habit: HabitEntity): Long = habitDao.insertHabit(habit)
    suspend fun updateHabit(habit: HabitEntity) = habitDao.updateHabit(habit)
    suspend fun deleteHabit(habit: HabitEntity) = habitDao.deleteHabit(habit)

    fun getCompletionsByDate(date: String): Flow<List<CompletionEntity>> =
        completionDao.getCompletionsByDate(date)

    suspend fun toggleHabitCompletion(habitId: Long, date: String) {
        val existing = completionDao.getCompletion(habitId, date)
        if (existing != null) {
            completionDao.deleteCompletion(existing)
        } else {
            completionDao.insertCompletion(CompletionEntity(habitId = habitId, date = date))
        }
    }

    private fun getTodayDateString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }
}
