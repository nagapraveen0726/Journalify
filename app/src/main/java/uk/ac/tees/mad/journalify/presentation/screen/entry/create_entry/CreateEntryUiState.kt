package uk.ac.tees.mad.journalify.presentation.screen.entry.create_entry

data class CreateEntryUiState(
    val title: String = "",
    val content: String = "",
    val imagePath: String? = null,
    val loading: Boolean = false
)