package uk.ac.tees.mad.journalify.domain.usecase

import android.util.Log
import uk.ac.tees.mad.journalify.domain.model.JournalEntry
import uk.ac.tees.mad.journalify.domain.repository.LocalJournalRepository
import uk.ac.tees.mad.journalify.domain.repository.RemoteJournalRepository
import javax.inject.Inject

class SyncPullUseCase @Inject constructor(
    private val remote: RemoteJournalRepository,
    private val local: LocalJournalRepository
) {
    suspend operator fun invoke() {
        Log.d("SyncPullUseCase", "Starting sync pull")

        val cloud = remote.pullAll()
        Log.d("SyncPullUseCase", "Fetched ${cloud.size} entries from cloud")

        cloud.forEach { e ->
            val localEntry = JournalEntry(
                id = e.id,
                title = e.title,
                content = e.content,
                imagePath = null,           // No local path
                imageUrl = e.imageUrl,      // Map imageUrl from cloud
                createdAt = e.createdAt,
                updatedAt = e.updatedAt,
                isSynced = true
            )

            Log.d("SyncPullUseCase", "Inserting entry ${e.id} with imageUrl: ${e.imageUrl}")
            local.insert(localEntry)
        }

        Log.d("SyncPullUseCase", "✅ Sync pull completed")
    }
}