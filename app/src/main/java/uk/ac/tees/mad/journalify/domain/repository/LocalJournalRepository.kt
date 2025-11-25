package uk.ac.tees.mad.journalify.domain.repository

import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.journalify.domain.model.JournalEntry

interface LocalJournalRepository {

    suspend fun insert(entry: JournalEntry)
    suspend fun update(entry: JournalEntry)
    suspend fun delete(entry: JournalEntry)

    fun getAll(): Flow<List<JournalEntry>>
    fun getById(id: String): Flow<JournalEntry?>
    fun search(query: String): Flow<List<JournalEntry>>
    fun getUnsynced(): Flow<List<JournalEntry>>
}