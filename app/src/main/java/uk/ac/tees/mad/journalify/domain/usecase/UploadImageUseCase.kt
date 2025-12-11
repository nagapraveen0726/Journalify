package uk.ac.tees.mad.journalify.domain.usecase

import android.util.Log
import uk.ac.tees.mad.journalify.data.remote.CloudinaryUploader
import javax.inject.Inject

class UploadImageUseCase @Inject constructor(
    private val uploader: CloudinaryUploader
) {

    suspend operator fun invoke(
        entryId: String,
        localPath: String
    ): String {
        Log.d("UploadImageUseCase", "Invoked - EntryID: $entryId, Path: $localPath")

        return try {
            val url = uploader.upload(localPath, entryId)
            Log.d("UploadImageUseCase", "Success - URL: $url")
            url
        } catch (e: Exception) {
            Log.e("UploadImageUseCase", "Failed to upload image", e)
            throw e
        }
    }
}