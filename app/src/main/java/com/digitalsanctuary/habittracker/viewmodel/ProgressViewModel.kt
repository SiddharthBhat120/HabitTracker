package com.digitalsanctuary.habittracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.digitalsanctuary.habittracker.data.local.HabitDatabase
import com.digitalsanctuary.habittracker.data.local.entity.CompletionEntity
import com.digitalsanctuary.habittracker.data.local.entity.HabitEntity
import com.digitalsanctuary.habittracker.data.repository.HabitRepository
import com.digitalsanctuary.habittracker.utils.DateUtils
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ProgressViewModel(application: Application) : AndroidViewModel(application) {
    private val db = HabitDatabase.getDatabase(application)
    private val repository = HabitRepository(db.habitDao(), db.completionDao())

    val allHabits: StateFlow<List<HabitEntity>> = repository.allHabits
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCompletions: StateFlow<List<CompletionEntity>> = repository.allCompletions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedDate = MutableStateFlow(DateUtils.getTodayDateString())
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    /**
     * Habits for the selected date. 
     * Filters habits based on their creation date and shows completion status for that specific date.
     */
    val habitsForSelectedDate: StateFlow<List<Pair<HabitEntity, Boolean>>> = 
        combine(allHabits, allCompletions, selectedDate) { habits, completions, dateStr ->
            val dateObj = DateUtils.parseIsoDate(dateStr)
            val dateTime = dateObj?.time ?: 0L
            
            // Habits created on or before this day
            val dayEnd = dateTime + 86400000L
            val availableHabits = habits.filter { it.createdDate < dayEnd }
            
            val completionsForDate = completions.filter { it.date == dateStr }
            val isToday = dateStr == DateUtils.getTodayDateString()

            // For past dates, if no completions were recorded, we show it as empty (no activity)
            if (!isToday && completionsForDate.isEmpty()) {
                emptyList()
            } else {
                availableHabits.map { habit ->
                    val isCompleted = completionsForDate.any { it.habitId == habit.id }
                    habit to isCompleted
                }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /**
     * Day Streak: Counts consecutive days where ALL available habits were completed,
     * starting from today (or yesterday if today is not yet fully completed).
     */
    val streak: StateFlow<Int> = combine(allHabits, allCompletions) { habits, completions ->
        calculateStreak(habits, completions)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    /**
     * Consistency: (Total Completed Habits / Total Expected Habits) * 100
     * Calculated over the last 7 days.
     */
    val consistency: StateFlow<Int> = combine(allHabits, allCompletions) { habits, completions ->
        calculateConsistency(habits, completions)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun selectDate(date: String) {
        _selectedDate.value = date
    }

    fun toggleHabitCompletion(habitId: Long, date: String) {
        viewModelScope.launch {
            repository.toggleHabitCompletion(habitId, date)
        }
    }

    private fun calculateStreak(habits: List<HabitEntity>, completions: List<CompletionEntity>): Int {
        if (habits.isEmpty()) return 0
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        var streakCount = 0
        
        while (true) {
            val dateStr = sdf.format(calendar.time)
            val dayEnd = calendar.timeInMillis + 86400000L
            
            val habitsOnThisDay = habits.filter { it.createdDate < dayEnd }
            if (habitsOnThisDay.isEmpty()) break

            val completionsForDate = completions.filter { it.date == dateStr }
            val allCompleted = habitsOnThisDay.all { habit -> 
                completionsForDate.any { it.habitId == habit.id } 
            }
            
            if (allCompleted) {
                streakCount++
                calendar.add(Calendar.DAY_OF_YEAR, -1)
            } else {
                // Don't break streak if today isn't finished yet, but yesterday was completed.
                if (dateStr == sdf.format(Date())) {
                    calendar.add(Calendar.DAY_OF_YEAR, -1)
                    continue 
                } else {
                    break
                }
            }
        }
        return streakCount
    }

    private fun calculateConsistency(habits: List<HabitEntity>, completions: List<CompletionEntity>): Int {
        if (habits.isEmpty()) return 0
        
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        var totalExpected = 0
        var totalCompleted = 0
        
        // Calculate over the last 7 days as a default window
        for (i in 0 until 7) {
            val dateStr = sdf.format(calendar.time)
            val dayEnd = calendar.timeInMillis + 86400000L
            
            // Expected: Habits that already existed on this specific day
            val habitsOnThisDayCount = habits.count { it.createdDate < dayEnd }
            totalExpected += habitsOnThisDayCount
            
            // Completed: Actual completion entries recorded for this day
            val completionsOnThisDayCount = completions.count { it.date == dateStr }
            totalCompleted += completionsOnThisDayCount
            
            calendar.add(Calendar.DAY_OF_YEAR, -1)
        }
        
        if (totalExpected == 0) return 0
        return ((totalCompleted.toFloat() / totalExpected) * 100).toInt().coerceIn(0, 100)
    }
}
