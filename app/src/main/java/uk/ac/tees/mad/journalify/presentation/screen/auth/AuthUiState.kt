package uk.ac.tees.mad.journalify.presentation.screen.auth

data class AuthUiState(
    val mode: AuthMode = AuthMode.LOGIN,

    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",

    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,

    val loading: Boolean = false,
    val success: Boolean = false
)
