package uk.ac.tees.mad.journalify.presentation.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.journalify.domain.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {

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
            setLoading(true)

            try {
                repo.login(_uiState.value.email, _uiState.value.password)
                setSuccess(onSuccess)
            } catch (e: Exception) {
                setError(e)
            }
        }
    }

    fun submitSignup(onSuccess: () -> Unit) {
        if (!validateSignup()) return

        viewModelScope.launch {
            setLoading(true)

            try {
                repo.signup(_uiState.value.email, _uiState.value.password)
                setSuccess(onSuccess)
            } catch (e: Exception) {
                setError(e)
            }
        }
    }

    fun submitReset(onSuccess: () -> Unit) {
        val email = _uiState.value.email
        if (!email.contains("@")) return

        viewModelScope.launch {
            setLoading(true)

            try {
                repo.reset(email)
                setSuccess(onSuccess)
            } catch (e: Exception) {
                setError(e)
            }
        }
    }

    private fun setError(e: Throwable) {
        _uiState.value = _uiState.value.copy(
            loading = false,
            emailError = readableError(e)
        )
    }

    private fun readableError(e: Throwable): String {
        return e.message ?: "Unknown error"
    }

    private fun setSuccess(onSuccess: () -> Unit) {
        _uiState.value = _uiState.value.copy(loading = false, success = true)
        onSuccess()
    }

    private fun setLoading(v: Boolean) {
        _uiState.value = _uiState.value.copy(loading = v)
    }

//    private suspend fun setLoading() {
//        _uiState.value = _uiState.value.copy(loading = true)
//        delay(1200)
//        _uiState.value = _uiState.value.copy(loading = false)
//    }
}
