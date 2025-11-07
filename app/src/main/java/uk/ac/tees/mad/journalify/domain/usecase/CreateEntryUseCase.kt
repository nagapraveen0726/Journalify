package uk.ac.tees.mad.journalify.domain.usecase

import uk.ac.tees.mad.journalify.domain.model.JournalEntry
import uk.ac.tees.mad.journalify.domain.repository.LocalJournalRepository
import java.util.UUID
import javax.inject.Inject

class CreateEntryUseCase @Inject constructor(
    private val repo: LocalJournalRepository
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

        repo.insert(entry)
    }
}
