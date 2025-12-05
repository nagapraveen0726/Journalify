package uk.ac.tees.mad.journalify.presentation.screen.entry.edit_entry

import uk.ac.tees.mad.journalify.domain.model.JournalEntry

data class EditEntryState(
    val entry: JournalEntry? = null,
    val title: String = "",
    val content: String = "",
    val imagePath: String? = null
)