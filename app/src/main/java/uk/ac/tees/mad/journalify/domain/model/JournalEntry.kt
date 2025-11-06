package uk.ac.tees.mad.journalify.domain.model

data class JournalEntry(
    val id: String,
    val title: String,
    val content: String,
    val imagePath: String?,
    val createdAt: Long,
    val updatedAt: Long,
    val isSynced: Boolean
)
