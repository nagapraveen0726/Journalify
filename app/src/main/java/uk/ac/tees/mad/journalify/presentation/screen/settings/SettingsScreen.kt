package uk.ac.tees.mad.journalify.presentation.screen.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import uk.ac.tees.mad.journalify.presentation.components.ScreenPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBack: () -> Unit = {},
    onLoggedOut: () -> Unit = {}
) {
    val ui by viewModel.ui.collectAsState()
    val scrollState = rememberScrollState()
    var showLogoutDialog = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier
                            .padding(12.dp)
                            .clickable(onClick = onBack)
                    )
                }
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
//                .padding(paddingValues)
        ) {

            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 12.dp, vertical = 16.dp)
            ) {

                if (ui.isSaving) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )
                }

                // Account section
                SectionTitle(text = "Account")

                AvatarRow(
                    displayName = ui.displayName,
                    onClick = { viewModel.onAvatarClick() }
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "Display name",
                    style = MaterialTheme.typography.labelLarge
                )

                Spacer(Modifier.height(4.dp))

                OutlinedTextField(
                    value = ui.displayName,
                    onValueChange = viewModel::updateDisplayName,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(Modifier.height(24.dp))

                // Personalization
                SectionTitle(text = "Personalization")

                SettingsSwitchRow(
                    title = "Dark theme",
                    description = "Use dark appearance for the app",
                    checked = ui.themeDark,
                    onCheckedChange = viewModel::updateTheme
                )

//            Spacer(Modifier.height(24.dp))
//
//            // Security
//            SectionTitle(text = "Security")
//
//            SettingsSwitchRow(
//                title = "Biometric unlock",
//                description = "Unlock the app using fingerprint / face",
//                checked = ui.biometricEnabled,
//                onCheckedChange = viewModel::updateBiometric
//            )

                Spacer(Modifier.height(24.dp))

                // Sync & storage
                SectionTitle(text = "Sync & storage")

                SettingsSwitchRow(
                    title = "Auto-sync",
                    description = "Automatically sync your data in the background",
                    checked = ui.autoSync,
                    onCheckedChange = viewModel::updateAutoSync
                )

                Spacer(Modifier.height(12.dp))

                StorageUsageRow(
                    storageUsage = ui.storageUsage,
                    onRefresh = { viewModel.refreshStorageUsage() }
                )

                Spacer(Modifier.height(12.dp))

                TextButton(
                    onClick = { viewModel.clearCachedImages() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Clear cached images")
                }

                Spacer(Modifier.height(32.dp))

                // Session
//            SectionTitle(text = "Session")

            }
            Button(
                onClick = { showLogoutDialog.value = true },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp)
                    .fillMaxWidth(0.85f)
            ) {
                Text("Logout")
            }

//            Spacer(Modifier.height(16.dp))

            if (showLogoutDialog.value) {
                AlertDialog(
                    onDismissRequest = { showLogoutDialog.value = false },
                    title = { Text("Logout") },
                    text = { Text("Are you sure you want to logout and clear your session?") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showLogoutDialog.value = false
                                viewModel.logout(onLoggedOut)
                            }
                        ) {
                            Text("Logout")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showLogoutDialog.value = false }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }

        }

    }
}

@Composable
 fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium
    )
}

@Composable
 fun AvatarRow(
    displayName: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            val initials = displayName.trim()
                .takeIf { it.isNotEmpty() }
                ?.first()
                ?.uppercase()
                ?: "?"

            Text(
                text = initials,
                style = MaterialTheme.typography.titleLarge
            )
        }

        Spacer(Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Avatar",
                style = MaterialTheme.typography.bodyLarge
            )
//            Text(
//                text = "Tap to change picture",
//                style = MaterialTheme.typography.bodySmall,
//                color = MaterialTheme.colorScheme.onSurfaceVariant
//            )
        }
    }
}

@Composable
 fun SettingsSwitchRow(
    title: String,
    description: String?,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            if (description != null) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun StorageUsageRow(
    storageUsage: String,
    onRefresh: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Storage usage",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = if (storageUsage.isBlank()) "Tap refresh to calculate" else storageUsage,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        TextButton(onClick = onRefresh) {
            Text("Refresh")
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SettingsScreenPreview() {
    ScreenPreview {
        SettingsScreen(
//            viewModel = object : SettingsViewModel(
//                session = object : uk.ac.tees.mad.journalify.data.session.SessionManager(
//                    context = android.app.Application()
//                ) {}
//            ) {} // You can ignore this in your real project; Hilt will provide the VM.
        )
    }
}
