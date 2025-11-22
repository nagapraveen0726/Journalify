package uk.ac.tees.mad.journalify.presentation.screen.settings

data class SettingsUiState(
    val displayName: String = "",
    val avatar: String? = null,
    val themeDark: Boolean = false,
    val biometricEnabled: Boolean = false,
    val autoSync: Boolean = true,
    val isSaving: Boolean = false
)