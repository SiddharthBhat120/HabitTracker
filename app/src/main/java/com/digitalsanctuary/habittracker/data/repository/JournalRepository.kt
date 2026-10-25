package com.digitalsanctuary.habittracker.data.repository
import com.digitalsanctuary.habittracker.data.local.dao.JournalDao
import com.digitalsanctuary.habittracker.data.local.entity.JournalEntry
import kotlinx.coroutines.flow.Flow

class JournalRepository(private val journalDao: JournalDao) {
    val allEntries: Flow<List<JournalEntry>> = journalDao.getAllEntries()
    suspend fun insertEntry(entry: JournalEntry): Long = journalDao.insertEntry(entry)
    suspend fun deleteEntry(entry: JournalEntry) = journalDao.deleteEntry(entry)
}
