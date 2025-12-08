package uk.ac.tees.mad.journalify.presentation.screen.settings

data class SettingsUiState(
    val displayName: String = "",
    val avatar: String? = null,          // URL or path – up to you later
    val themeDark: Boolean = false,
    val biometricEnabled: Boolean = false,
    val autoSync: Boolean = true,
    val storageUsage: String = "",       // e.g. "24.3 MB"
    val isSaving: Boolean = false
)
