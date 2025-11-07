package uk.ac.tees.mad.journalify.domain.usecase

import uk.ac.tees.mad.journalify.domain.model.JournalEntry
import uk.ac.tees.mad.journalify.domain.repository.LocalJournalRepository
import javax.inject.Inject

class DeleteEntryUseCase @Inject constructor(
    private val repo: LocalJournalRepository
) {
    suspend operator fun invoke(entry: JournalEntry) {
        repo.delete(entry)
    }
}
