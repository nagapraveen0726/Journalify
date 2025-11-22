package uk.ac.tees.mad.journalify.domain.model

data class CloudJournalEntry(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val imageUrl: String? = null,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
)