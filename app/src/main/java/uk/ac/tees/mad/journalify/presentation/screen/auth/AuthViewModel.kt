package uk.ac.tees.mad.journalify.presentation.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    fun switchMode(mode: AuthMode) {
        _uiState.value = _uiState.value.copy(
            mode = mode,
            emailError = null,
            passwordError = null,
            confirmPasswordError = null,
            success = false
        )
    }

    fun updateEmail(value: String) {
        _uiState.value = _uiState.value.copy(email = value, emailError = null)
    }

    fun updatePassword(value: String) {
        _uiState.value = _uiState.value.copy(password = value, passwordError = null)
    }

    fun updateConfirmPassword(value: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = value, confirmPasswordError = null)
    }

    private fun validateLogin(): Boolean {
        val state = _uiState.value
        var ok = true

        if (!state.email.contains("@")) {
            ok = false
            _uiState.value = state.copy(emailError = "Invalid email")
        }

        if (state.password.length < 6) {
            ok = false
            _uiState.value = state.copy(passwordError = "Password too short")
        }

        return ok
    }

    private fun validateSignup(): Boolean {
        val state = _uiState.value
        var ok = validateLogin()

        if (state.password != state.confirmPassword) {
            ok = false
            _uiState.value = state.copy(confirmPasswordError = "Passwords do not match")
        }

        return ok
    }

    fun submitLogin(onSuccess: () -> Unit) {
        if (!validateLogin()) return

        viewModelScope.launch {
            simulateLoading()
            onSuccess()
            _uiState.value = _uiState.value.copy(success = true)
        }
    }

    fun submitSignup(onSuccess: () -> Unit) {
        if (!validateSignup()) return

        viewModelScope.launch {
            simulateLoading()
            onSuccess()
            _uiState.value = _uiState.value.copy(success = true)
        }
    }

    fun submitReset(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (!state.email.contains("@")) {
            _uiState.value = state.copy(emailError = "Invalid email")
            return
        }

        viewModelScope.launch {
            simulateLoading()
            onSuccess()
            _uiState.value = state.copy(success = true)
        }
    }

    private suspend fun simulateLoading() {
        _uiState.value = _uiState.value.copy(loading = true)
        delay(1200)
        _uiState.value = _uiState.value.copy(loading = false)
    }
}
