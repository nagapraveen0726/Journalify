package uk.ac.tees.mad.journalify.domain.usecase

import uk.ac.tees.mad.journalify.domain.model.CloudJournalEntry
import uk.ac.tees.mad.journalify.domain.model.JournalEntry
import uk.ac.tees.mad.journalify.domain.repository.LocalJournalRepository
import uk.ac.tees.mad.journalify.domain.repository.RemoteJournalRepository
import java.util.UUID
import javax.inject.Inject

class CreateEntryUseCase @Inject constructor(
    private val local: LocalJournalRepository,
    private val remote: RemoteJournalRepository
) {
    suspend operator fun invoke(
        title: String,
        content: String,
        imagePath: String?
    ) {
        val now = System.currentTimeMillis()

        val entry = JournalEntry(
            id = UUID.randomUUID().toString(),
            title = title,
            content = content,
            imagePath = imagePath,
            createdAt = now,
            updatedAt = now,
            isSynced = false
        )

        local.insert(entry)

        remote.push(
            CloudJournalEntry(
                id = entry.id,
                title = entry.title,
                content = entry.content,
                imageUrl = entry.imagePath,
                createdAt = entry.createdAt,
                updatedAt = entry.updatedAt
            )
        )
    }
}
