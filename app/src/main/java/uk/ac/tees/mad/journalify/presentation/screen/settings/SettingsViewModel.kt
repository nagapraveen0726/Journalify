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
            val bio = session.biometricEnabled.first()
            _ui.value = _ui.value.copy(
                displayName = name,
                biometricEnabled = bio
            )
        }
    }

    fun updateDisplayName(v: String) {
        _ui.value = _ui.value.copy(displayName = v)
    }

    fun updateTheme(v: Boolean) {
        _ui.value = _ui.value.copy(themeDark = v)
    }

    fun updateAutoSync(v: Boolean) {
        _ui.value = _ui.value.copy(autoSync = v)
    }

    fun logout(onDone: () -> Unit) {
        viewModelScope.launch {
            session.clearSession()
            onDone()
        }
    }

}
