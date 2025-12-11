package uk.ac.tees.mad.journalify.domain.usecase

import android.util.Log
import kotlinx.coroutines.flow.first
import uk.ac.tees.mad.journalify.domain.model.CloudJournalEntry
import uk.ac.tees.mad.journalify.domain.repository.LocalJournalRepository
import uk.ac.tees.mad.journalify.domain.repository.RemoteJournalRepository
import javax.inject.Inject

class SyncPushUseCase @Inject constructor(
    private val remote: RemoteJournalRepository,
    private val local: LocalJournalRepository
) {  // Remove UploadImageUseCase dependency

    suspend operator fun invoke() {
        Log.d("SyncPushUseCase", "Starting sync push")

        val unsynced = local.getUnsynced().first()
        Log.d("SyncPushUseCase", "Found ${unsynced.size} unsynced entries")

        if (unsynced.isEmpty()) return

        unsynced.forEach { e ->
            Log.d("SyncPushUseCase", "Processing entry: ${e.id}")
            Log.d("SyncPushUseCase", "imageUrl: ${e.imageUrl}, imagePath: ${e.imagePath}")

            try {
                // Image is already uploaded during creation, just push metadata
                val cloud = CloudJournalEntry(
                    id = e.id,
                    title = e.title,
                    content = e.content,
                    imageUrl = e.imageUrl,  // Use imageUrl, which has the Cloudinary URL
                    createdAt = e.createdAt,
                    updatedAt = e.updatedAt
                )

                Log.d("SyncPushUseCase", "Pushing to remote with imageUrl: ${e.imageUrl}")
                remote.push(cloud)

                // Just mark as synced, don't modify image fields
                val updated = e.copy(isSynced = true)
                Log.d("SyncPushUseCase", "Marking as synced")
                local.update(updated)

                Log.d("SyncPushUseCase", "✅ Successfully synced entry ${e.id}")

            } catch (ex: Exception) {
                Log.e("SyncPushUseCase", "❌ Failed to sync entry ${e.id}", ex)
            }
        }

        Log.d("SyncPushUseCase", "Sync push completed")
    }
}