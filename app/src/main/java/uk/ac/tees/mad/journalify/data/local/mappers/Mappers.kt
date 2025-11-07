package uk.ac.tees.mad.journalify.data.local.mappers

import uk.ac.tees.mad.journalify.data.local.entity.JournalEntryEntity
import uk.ac.tees.mad.journalify.domain.model.JournalEntry

fun JournalEntryEntity.toDomain() =
    JournalEntry(id, title, content, imagePath, createdAt, updatedAt, isSynced)

fun JournalEntry.toEntity() =
    JournalEntryEntity(id, title, content, imagePath, createdAt, updatedAt, isSynced)