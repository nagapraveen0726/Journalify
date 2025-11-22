package uk.ac.tees.mad.journalify.data.session

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("journalify_session")

class SessionManager(private val context: Context) {

    companion object {
        private val KEY_USER_ID = stringPreferencesKey("user_id")
        private val KEY_LOGGED_IN = booleanPreferencesKey("logged_in")
        private val KEY_BIOMETRIC = booleanPreferencesKey("biometric_enabled")
    }

    val userId: Flow<String?> = context.dataStore.data.map { it[KEY_USER_ID] }
    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { it[KEY_LOGGED_IN] ?: false }
    val biometricEnabled: Flow<Boolean> = context.dataStore.data.map { it[KEY_BIOMETRIC] ?: false }

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

}