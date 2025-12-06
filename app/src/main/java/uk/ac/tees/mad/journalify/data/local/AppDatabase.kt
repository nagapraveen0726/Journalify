package uk.ac.tees.mad.journalify.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import uk.ac.tees.mad.journalify.data.local.dao.JournalEntryDao
import uk.ac.tees.mad.journalify.data.local.entity.JournalEntryEntity

@Database(
    entities = [JournalEntryEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun journalEntryDao(): JournalEntryDao
}