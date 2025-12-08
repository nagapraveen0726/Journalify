package uk.ac.tees.mad.journalify.presentation.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import uk.ac.tees.mad.journalify.data.session.SessionManager
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val session: SessionManager
) : ViewModel() {

    private val _ui = MutableStateFlow(SettingsUiState())
    val ui: StateFlow<SettingsUiState> = _ui

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            val name = session.userId.first() ?: ""

            // TODO: Load avatar, theme, biometric, autoSync, storageUsage
            _ui.value = _ui.value.copy(
                displayName = name
            )
        }
    }

    fun updateDisplayName(value: String) {
        _ui.value = _ui.value.copy(displayName = value)
        // TODO: Persist to Firestore + local settings
    }

    fun updateTheme(isDark: Boolean) {
        _ui.value = _ui.value.copy(themeDark = isDark)
        // TODO: Persist theme preference to DataStore
    }

    fun updateAutoSync(enabled: Boolean) {
        _ui.value = _ui.value.copy(autoSync = enabled)
        // TODO: Persist auto-sync preference
    }

    fun updateBiometric(enabled: Boolean) {
        _ui.value = _ui.value.copy(biometricEnabled = enabled)
        // TODO: Enable / disable biometric lock in settings
    }

    fun onAvatarClick() {
        // TODO: Trigger avatar picker (e.g. via events / callbacks)
    }

    fun refreshStorageUsage() {
        viewModelScope.launch {
            // TODO: Calculate real storage usage
            // _ui.value = _ui.value.copy(storageUsage = "... MB")
        }
    }

    fun clearCachedImages() {
        viewModelScope.launch {
            // TODO: Clear image/cache storage
            // Optionally update storageUsage afterwards
        }
    }

    fun logout(onDone: () -> Unit) {
        viewModelScope.launch {
            // TODO: Clear local DB and any other local state
            session.clearSession()
            onDone()
        }
    }
}
