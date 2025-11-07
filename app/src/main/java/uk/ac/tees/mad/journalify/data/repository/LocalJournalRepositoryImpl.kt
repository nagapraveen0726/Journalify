package uk.ac.tees.mad.journalify.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uk.ac.tees.mad.journalify.data.local.dao.JournalEntryDao
import uk.ac.tees.mad.journalify.data.local.mappers.toDomain
import uk.ac.tees.mad.journalify.data.local.mappers.toEntity
import uk.ac.tees.mad.journalify.domain.model.JournalEntry
import uk.ac.tees.mad.journalify.domain.repository.LocalJournalRepository
import javax.inject.Inject

class LocalJournalRepositoryImpl @Inject constructor(
    private val dao: JournalEntryDao
): LocalJournalRepository {

    override suspend fun insert(entry: JournalEntry) {
        dao.insert(entry.toEntity())
    }

    override suspend fun update(entry: JournalEntry) {
        dao.update(entry.toEntity())
    }

    override suspend fun delete(entry: JournalEntry) {
        dao.delete(entry.toEntity())
    }

    override fun getAll(): Flow<List<JournalEntry>> =
        dao.getAll().map { it.map { e -> e.toDomain() } }

    override fun getById(id: String): Flow<JournalEntry?> =
        dao.getById(id).map { it?.toDomain() }

    override fun search(query: String): Flow<List<JournalEntry>> =
        dao.search(query).map { it.map { e -> e.toDomain() } }
}
