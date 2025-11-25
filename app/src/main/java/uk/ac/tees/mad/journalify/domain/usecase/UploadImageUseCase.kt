package uk.ac.tees.mad.journalify.domain.usecase

import uk.ac.tees.mad.journalify.data.remote.CloudinaryUploader
import javax.inject.Inject

class UploadImageUseCase @Inject constructor(
    private val uploader: CloudinaryUploader
) {

    suspend operator fun invoke(
        entryId: String,
        localPath: String
    ): String {

        return uploader.upload(localPath, entryId)
    }
}
