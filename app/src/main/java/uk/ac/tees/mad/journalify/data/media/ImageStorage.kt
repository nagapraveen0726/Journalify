package uk.ac.tees.mad.journalify.data.media

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject

class ImageStorage @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun save(file: File): String {
        val name = file.name
        val dest = File(context.filesDir, name)
        file.copyTo(dest, overwrite = true)
        return dest.absolutePath
    }

    fun load(path: String): Bitmap? {
        return BitmapFactory.decodeFile(path)
    }
}
