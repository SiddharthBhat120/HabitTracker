package com.digitalsanctuary.habittracker.data.local.entity
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journal_entries")
data class JournalEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val content: String,
    val mood: String = "NEUTRAL",
    val createdAt: Long = System.currentTimeMillis()
)
