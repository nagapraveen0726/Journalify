package uk.ac.tees.mad.journalify.domain.usecase

import uk.ac.tees.mad.journalify.domain.model.JournalEntry
import uk.ac.tees.mad.journalify.domain.repository.LocalJournalRepository
import uk.ac.tees.mad.journalify.domain.repository.RemoteJournalRepository
import javax.inject.Inject

class SyncPullUseCase @Inject constructor(
    private val remote: RemoteJournalRepository,
    private val local: LocalJournalRepository
) {
    suspend operator fun invoke() {

        val cloud = remote.pullAll()

        cloud.forEach { e ->
            val localEntry = JournalEntry(
                id = e.id,
                title = e.title,
                content = e.content,
                imagePath = null, // image URLs later
                createdAt = e.createdAt,
                updatedAt = e.updatedAt,
                isSynced = true
            )

            local.insert(localEntry)
        }
    }
}