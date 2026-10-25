package com.digitalsanctuary.habittracker.data.local.entity
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String = "",
    val frequency: String = "DAILY",
    val duration: Int = 15, // renmaed from targetMinutes
    val reminderEnabled: Boolean = true,
    val reminderTime: String = "08:00",
    val iconName: String = "check_circle",
    val colorHex: String = "#2F6B8A",
    val createdDate: Long = System.currentTimeMillis(), // renamed from createdAt
    val isCompletedToday: Boolean = false,
    val lastCompletedDate: Long = 0L,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val totalCompletions: Int = 0,
    val bestMonth: String = ""
)
