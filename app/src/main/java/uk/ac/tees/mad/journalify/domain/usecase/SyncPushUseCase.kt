package uk.ac.tees.mad.journalify.domain.usecase

import android.util.Log
import kotlinx.coroutines.flow.first
import uk.ac.tees.mad.journalify.domain.model.CloudJournalEntry
import uk.ac.tees.mad.journalify.domain.repository.LocalJournalRepository
import uk.ac.tees.mad.journalify.domain.repository.RemoteJournalRepository
import javax.inject.Inject

class SyncPushUseCase @Inject constructor(
    private val remote: RemoteJournalRepository,
    private val local: LocalJournalRepository,
    private val uploadImage: UploadImageUseCase
) {

    suspend operator fun invoke() {

        val unsynced = local.getUnsynced().first()

        if (unsynced.isEmpty()) return

        unsynced.forEach { e ->

            val url = e.imagePath?.let {
                uploadImage(e.id, it)
            }

            val cloud = CloudJournalEntry(
                id = e.id,
                title = e.title,
                content = e.content,
                imageUrl = url,
                createdAt = e.createdAt,
                updatedAt = e.updatedAt
            )
            remote.push(cloud)

            val updated = e.copy(imagePath = url, isSynced = true)
            local.update(updated)
            Log.d("UPLOAD", "Uploaded: $url")
        }
    }
}
