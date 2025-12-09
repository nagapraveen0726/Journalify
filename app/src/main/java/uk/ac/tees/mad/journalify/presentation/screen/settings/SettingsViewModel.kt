package uk.ac.tees.mad.journalify.presentation.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import uk.ac.tees.mad.journalify.data.session.SessionManager
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val session: SessionManager
) : ViewModel() {

    private val _ui = MutableStateFlow(SettingsUiState())
    val ui: StateFlow<SettingsUiState> = _ui

    private val _working = MutableStateFlow(false)
    val working: StateFlow<Boolean> = _working.asStateFlow()

    init {
//        load()
        observeSettings()
    }

    private fun observeSettings() {
        // collect initial values and subsequent updates
        viewModelScope.launch {
            // Combine several flows to one UI state
            combine(
                session.displayName,
                session.themeDark,
                session.autoSync,
                session.avatarPath
            ) { name, theme, autoSync, avatar ->
                Triple(name ?: "", Pair(theme, autoSync), avatar)
            }.collect { triple ->
                val (name, themeAuto, avatar) = triple
                val (theme, auto) = themeAuto
                _ui.value = _ui.value.copy(
                    displayName = name,
                    themeDark = theme,
                    autoSync = auto,
                    avatar = avatar
                )
            }
        }

        // storage usage loaded separately
        refreshStorageUsage()
    }

//    private fun load() {
//        viewModelScope.launch {
//            val name = session.userId.first() ?: ""
//
//            // TODO: Load avatar, theme, biometric, autoSync, storageUsage
//            _ui.value = _ui.value.copy(
//                displayName = name
//            )
//        }
//    }

    fun saveDisplayName(value: String) {
        viewModelScope.launch {
            _working.value = true
            session.setDisplayName(value)
            _working.value = false
        }
    }

    fun cancelDisplayNameEdit() {
        // Nothing special — UI will reload displayName from flow
    }

    // --- Theme
    fun updateTheme(isDark: Boolean) {
        viewModelScope.launch {
            session.setThemeDark(isDark)
            // UI will observe the change
        }
    }

    // --- Auto-sync
    fun updateAutoSync(enabled: Boolean) {
        viewModelScope.launch {
            session.setAutoSync(enabled)
        }
    }

    // --- Avatar (local-only)
    fun setAvatar(path: String?) {
        viewModelScope.launch {
            session.setAvatarPath(path)
        }
    }

    // --- Storage usage and clear cache
    fun refreshStorageUsage() {
        viewModelScope.launch {
            _working.value = true
            val usage = session.getStorageUsage()
            _ui.value = _ui.value.copy(storageUsage = usage)
            _working.value = false
        }
    }

    fun clearCachedImages() {
        viewModelScope.launch {
            _working.value = true
            val deleted = session.clearCachedImages()
            // update usage after deletion
            val usage = session.getStorageUsage()
            _ui.value = _ui.value.copy(storageUsage = usage)
            _working.value = false
        }
    }

    // --- Avatar click: normally you'd fire an event to the UI to open a picker.
    // Here we expose an event flow for UI to react to.
    private val _pickAvatarEvent = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val pickAvatarEvent: SharedFlow<Unit> = _pickAvatarEvent.asSharedFlow()

    fun onAvatarClick() {
        _pickAvatarEvent.tryEmit(Unit)
    }

    // --- Logout
    fun logout(onDone: () -> Unit) {
        viewModelScope.launch {
            _working.value = true
            // clear session (DataStore) - this removes local profile settings too
            session.clearSession()
            _working.value = false
            onDone()
        }
    }
}
