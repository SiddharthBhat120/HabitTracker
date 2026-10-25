package com.digitalsanctuary.habittracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.digitalsanctuary.habittracker.data.local.HabitDatabase
import com.digitalsanctuary.habittracker.data.local.entity.HabitEntity
import com.digitalsanctuary.habittracker.data.local.entity.JournalEntry
import com.digitalsanctuary.habittracker.data.repository.HabitRepository
import com.digitalsanctuary.habittracker.data.repository.JournalRepository
import com.digitalsanctuary.habittracker.utils.DateUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HabitViewModel(application: Application) : AndroidViewModel(application) {
    private val db = HabitDatabase.getDatabase(application)
    private val habitRepo = HabitRepository(db.habitDao(), db.completionDao())
    private val journalRepo = JournalRepository(db.journalDao())

    val allHabits: StateFlow<List<HabitEntity>> = habitRepo.allHabits
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
        
    @OptIn(ExperimentalCoroutinesApi::class)
    val completionsToday: StateFlow<List<Long>> = flow {
        while (true) {
            emit(DateUtils.getTodayDateString())
            delay(30000) // Check for date change every 30 seconds
        }
    }.distinctUntilChanged()
    .flatMapLatest { date ->
        habitRepo.getCompletionsByDate(date)
            .map { list -> list.map { it.habitId } }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val journalEntries: StateFlow<List<JournalEntry>> = journalRepo.allEntries
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun getHabitById(id: Long): Flow<HabitEntity?> = habitRepo.getHabitById(id)

    fun addHabit(name: String, frequency: String, duration: Int, reminderEnabled: Boolean, reminderTime: String) {
        viewModelScope.launch {
            val icons = mapOf(
                "meditat" to "self_improvement", "read" to "menu_book", "exercise" to "fitness_center",
                "run" to "directions_run", "water" to "water_drop", "hydrat" to "water_drop",
                "sleep" to "bedtime", "journal" to "edit_note", "work" to "work",
                "study" to "school", "yoga" to "self_improvement"
            )
            val lname = name.lowercase()
            val icon = icons.entries.firstOrNull { lname.contains(it.key) }?.value ?: "check_circle"
            habitRepo.insertHabit(HabitEntity(
                name = name, frequency = frequency, duration = duration,
                reminderEnabled = reminderEnabled, reminderTime = reminderTime, iconName = icon
            ))
        }
    }

    fun toggleHabit(habitId: Long) {
        viewModelScope.launch {
            habitRepo.toggleHabitCompletion(habitId, DateUtils.getTodayDateString())
        }
    }

    fun deleteHabit(habit: HabitEntity) { viewModelScope.launch { habitRepo.deleteHabit(habit) } }

    fun addJournalEntry(title: String, content: String, mood: String) {
        viewModelScope.launch { journalRepo.insertEntry(JournalEntry(title = title, content = content, mood = mood)) }
    }
    fun deleteJournalEntry(entry: JournalEntry) { viewModelScope.launch { journalRepo.deleteEntry(entry) } }
}
