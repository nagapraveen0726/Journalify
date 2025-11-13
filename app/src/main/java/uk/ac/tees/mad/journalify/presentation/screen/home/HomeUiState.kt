package uk.ac.tees.mad.journalify.presentation.screen.home

import uk.ac.tees.mad.journalify.domain.model.JournalEntry

data class HomeUiState(
    val isLoading: Boolean = true,
    val entries: List<JournalEntry> = emptyList(),
    val query: String = "",
)