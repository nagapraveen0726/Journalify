package uk.ac.tees.mad.journalify.presentation.previews

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uk.ac.tees.mad.journalify.presentation.screen.settings.AvatarRow
import uk.ac.tees.mad.journalify.presentation.screen.settings.SectionTitle
import uk.ac.tees.mad.journalify.presentation.screen.settings.SettingsSwitchRow
import uk.ac.tees.mad.journalify.presentation.screen.settings.StorageUsageRow

@Composable
fun SettingsScreen_UI(
    displayName: String,
    themeDark: Boolean,
    autoSync: Boolean,
    storageUsage: String,
    isSaving: Boolean = false,

    onDisplayNameChange: (String) -> Unit = {},
    onThemeChange: (Boolean) -> Unit = {},
    onAutoSyncChange: (Boolean) -> Unit = {},
    onRefreshStorage: () -> Unit = {},
    onClearCache: () -> Unit = {},
    onAvatarClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {

    val scroll = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        // scroll content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(scroll)
        ) {

            if (isSaving) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            }

            SectionTitle("Account")

            AvatarRow(
                displayName = displayName,
                onClick = onAvatarClick
            )

            Spacer(Modifier.height(12.dp))

            Text("Display name")
            OutlinedTextField(
                value = displayName,
                onValueChange = onDisplayNameChange,
                modifier = Modifier.fillMaxWidth()
            )


            Spacer(Modifier.height(24.dp))

            SectionTitle("Personalization")

            SettingsSwitchRow(
                title = "Dark theme",
                description = "Use dark UI",
                checked = themeDark,
                onCheckedChange = onThemeChange
            )


            Spacer(Modifier.height(24.dp))

            SectionTitle("Sync & Storage")

            SettingsSwitchRow(
                title = "Auto-sync",
                description = "Sync in background",
                checked = autoSync,
                onCheckedChange = onAutoSyncChange
            )

            Spacer(Modifier.height(12.dp))

            StorageUsageRow(
                storageUsage = storageUsage,
                onRefresh = onRefreshStorage
            )

            Spacer(Modifier.height(12.dp))

            TextButton(
                onClick = onClearCache,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Clear cached images")
            }

            Spacer(Modifier.height(100.dp))
        }

        Button(
            onClick = onLogoutClick,
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        ) {
            Text("Logout")
        }
    }
}
