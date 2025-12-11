package uk.ac.tees.mad.journalify.data.remote

import android.content.Context
import android.net.Uri
import android.util.Log
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class CloudinaryUploader @Inject constructor(
    private val cloud: Cloudinary,
    @ApplicationContext private val context: Context
) {

    suspend fun upload(localPath: String, entryId: String): String {
        Log.d("CloudinaryUploader", "Starting upload - Path: $localPath, EntryID: $entryId")

        return withContext(Dispatchers.IO) {
            try {
                // Handle content:// URIs
                if (localPath.startsWith("content://")) {
                    Log.d("CloudinaryUploader", "Detected Content URI")
                    uploadFromContentUri(Uri.parse(localPath), entryId)
                } else {
                    Log.d("CloudinaryUploader", "Detected File Path")
                    uploadFromFile(File(localPath), entryId)
                }
            } catch (e: Exception) {
                Log.e("CloudinaryUploader", "Upload failed", e)
                throw e
            }
        }
    }

    private fun uploadFromContentUri(uri: Uri, entryId: String): String {
        Log.d("CloudinaryUploader", "Opening input stream for URI: $uri")

        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Cannot open input stream for URI: $uri")

        val tempFile = File.createTempFile("journal_", ".jpg", context.cacheDir)
        Log.d("CloudinaryUploader", "Created temp file: ${tempFile.absolutePath}")

        try {
            inputStream.use { input ->
                FileOutputStream(tempFile).use { output ->
                    val bytes = input.copyTo(output)
                    Log.d("CloudinaryUploader", "Copied $bytes bytes to temp file")
                }
            }

            Log.d("CloudinaryUploader", "Temp file size: ${tempFile.length()} bytes")
            Log.d("CloudinaryUploader", "Starting Cloudinary upload...")

            val result = cloud.uploader().upload(
                tempFile,
                ObjectUtils.asMap(
                    "public_id", entryId,
                    "overwrite", true,
                    "folder", "journalify",
                    "resource_type", "auto"
                )
            )

            val url = result["secure_url"] as String
            Log.d("CloudinaryUploader", "Upload successful! URL: $url")
            return url

        } catch (e: Exception) {
            Log.e("CloudinaryUploader", "Error during Cloudinary upload", e)
            throw e
        } finally {
            val deleted = tempFile.delete()
            Log.d("CloudinaryUploader", "Temp file deleted: $deleted")
        }
    }

    private fun uploadFromFile(file: File, entryId: String): String {
        Log.d("CloudinaryUploader", "Checking file: ${file.absolutePath}")

        if (!file.exists()) {
            throw IllegalArgumentException("File does not exist: ${file.absolutePath}")
        }

        Log.d("CloudinaryUploader", "File exists, size: ${file.length()} bytes")
        Log.d("CloudinaryUploader", "Starting Cloudinary upload...")

        val result = cloud.uploader().upload(
            file,
            ObjectUtils.asMap(
                "public_id", entryId,
                "overwrite", true,
                "folder", "journalify",
                "resource_type", "auto"
            )
        )

        val url = result["secure_url"] as String
        Log.d("CloudinaryUploader", "Upload successful! URL: $url")
        return url
    }

    suspend fun delete(entryId: String) {
        withContext(Dispatchers.IO) {
            try {
                Log.d("CloudinaryUploader", "Deleting from Cloudinary: journalify/$entryId")
                cloud.uploader().destroy(
                    "journalify/$entryId",
                    ObjectUtils.emptyMap()
                )
                Log.d("CloudinaryUploader", "Delete successful")
            } catch (e: Exception) {
                Log.e("CloudinaryUploader", "Delete failed", e)
                throw e
            }
        }
    }
}