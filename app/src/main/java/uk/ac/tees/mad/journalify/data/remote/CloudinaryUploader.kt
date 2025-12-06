package uk.ac.tees.mad.journalify.data.remote

import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class CloudinaryUploader @Inject constructor(
    private val cloud: Cloudinary
) {

    suspend fun upload(localPath: String, entryId: String): String {

        return withContext(Dispatchers.IO) {
            val file = File(localPath)

            val result = cloud.uploader().upload(
                file,
                ObjectUtils.asMap(
                    "public_id", entryId,
                    "overwrite", true,
                    "folder", "journalify",
                )
            )

            result["secure_url"] as String
        }
    }

    suspend fun delete(entryId: String) {
        withContext(Dispatchers.IO) {
            cloud.uploader().destroy(
                "journalify/$entryId",
                ObjectUtils.emptyMap()
            )
        }
    }
}
