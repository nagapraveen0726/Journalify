package uk.ac.tees.mad.journalify.data.session

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File

private val Context.dataStore by preferencesDataStore("journalify_session")

class SessionManager(
    @ApplicationContext private val context: Context) {

    companion object {
        private val KEY_USER_ID = stringPreferencesKey("user_id")
        private val KEY_LOGGED_IN = booleanPreferencesKey("logged_in")

        private val KEY_DISPLAY_NAME = stringPreferencesKey("display_name")
        private val KEY_THEME_DARK = booleanPreferencesKey("theme_dark")
        private val KEY_AUTO_SYNC = booleanPreferencesKey("auto_sync")
        private val KEY_AVATAR_PATH = stringPreferencesKey("avatar_path")
    }

    val userId: Flow<String?> = context.dataStore.data.map { it[KEY_USER_ID] }
    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { it[KEY_LOGGED_IN] ?: false }

    val displayName: Flow<String?> = context.dataStore.data.map { it[KEY_DISPLAY_NAME] }
    val themeDark: Flow<Boolean> = context.dataStore.data.map { it[KEY_THEME_DARK] ?: false }
    val autoSync: Flow<Boolean> = context.dataStore.data.map { it[KEY_AUTO_SYNC] ?: true }
    val avatarPath: Flow<String?> = context.dataStore.data.map { it[KEY_AVATAR_PATH] }

    suspend fun setSession(userId: String) {
        context.dataStore.edit {
            it[KEY_USER_ID] = userId
            it[KEY_LOGGED_IN] = true
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit {
            it.clear()
        }
    }

    suspend fun setDisplayName(name: String) {
        context.dataStore.edit {
            it[KEY_DISPLAY_NAME] = name
        }
    }

    suspend fun setThemeDark(dark: Boolean) {
        context.dataStore.edit {
            it[KEY_THEME_DARK] = dark
        }
    }

    suspend fun setAutoSync(enabled: Boolean) {
        context.dataStore.edit {
            it[KEY_AUTO_SYNC] = enabled
        }
    }

    suspend fun setAvatarPath(path: String?) {
        context.dataStore.edit {
            if (path == null) it.remove(KEY_AVATAR_PATH) else it[KEY_AVATAR_PATH] = path
        }
    }

    suspend fun getStorageUsage(): String = withContext(Dispatchers.IO) {
        val imageExt = setOf("jpg", "jpeg", "png", "webp")
        var totalBytes = 0L

        fun accumulate(dir: File?) {
            if (dir == null || !dir.exists()) return
            dir.listFiles()?.forEach { f ->
                if (f.isDirectory) {
                    accumulate(f)
                } else {
                    val name = f.name.lowercase()
                    val ext = name.substringAfterLast('.', "")
                    if (imageExt.contains(ext)) totalBytes += f.length()
                }
            }
        }

        // app internal storage
        accumulate(context.filesDir)
        // cache
        accumulate(context.cacheDir)

        // Format
        val kb = totalBytes / 1024.0
        val mb = kb / 1024.0
        return@withContext when {
            mb >= 1 -> String.format("%.1f MB", mb)
            kb >= 1 -> String.format("%.1f KB", kb)
            else -> "$totalBytes B"
        }
    }

    /**
     * Deletes app-owned image files (filesDir + cacheDir) matching image extensions.
     * Returns number of files deleted.
     */
    suspend fun clearCachedImages(): Int = withContext(Dispatchers.IO) {
        val imageExt = setOf("jpg", "jpeg", "png", "webp")
        var deleted = 0

        fun deleteInDir(dir: File?) {
            if (dir == null || !dir.exists()) return
            dir.listFiles()?.forEach { f ->
                if (f.isDirectory) {
                    deleteInDir(f)
                    // optionally remove empty directories
                    if (f.listFiles()?.isEmpty() == true) f.delete()
                } else {
                    val name = f.name.lowercase()
                    val ext = name.substringAfterLast('.', "")
                    if (imageExt.contains(ext)) {
                        if (f.delete()) deleted++
                    }
                }
            }
        }

        deleteInDir(context.cacheDir)
        deleteInDir(context.filesDir)

        return@withContext deleted
    }

}