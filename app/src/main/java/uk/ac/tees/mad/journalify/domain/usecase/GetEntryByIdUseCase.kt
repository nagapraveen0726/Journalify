package uk.ac.tees.mad.journalify.domain.usecase

import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.journalify.domain.model.JournalEntry
import uk.ac.tees.mad.journalify.domain.repository.LocalJournalRepository
import javax.inject.Inject

class GetEntryByIdUseCase @Inject constructor(
    private val repo: LocalJournalRepository
) {
    operator fun invoke(id: String): Flow<JournalEntry?> = repo.getById(id)
}
