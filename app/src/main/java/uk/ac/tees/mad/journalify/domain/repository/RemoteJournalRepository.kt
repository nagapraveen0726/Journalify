package uk.ac.tees.mad.journalify.domain.repository

import uk.ac.tees.mad.journalify.domain.model.CloudJournalEntry

interface RemoteJournalRepository {

    suspend fun push(entry: CloudJournalEntry)

    suspend fun pullAll(): List<CloudJournalEntry>

    suspend fun delete(id: String)
}
