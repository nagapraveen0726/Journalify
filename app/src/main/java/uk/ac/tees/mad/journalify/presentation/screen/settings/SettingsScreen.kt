package uk.ac.tees.mad.journalify.presentation.screen.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import uk.ac.tees.mad.journalify.presentation.components.ScreenPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBack: () -> Unit = {},
    onLoggedOut: () -> Unit = {}
) {
    val ui by viewModel.ui.collectAsState()
    val working by viewModel.working.collectAsState()
    val scroll = rememberScrollState()

    // display name editing state (local only)
    var editingName by remember { mutableStateOf(false) }
    var nameDraft by remember { mutableStateOf("") }

    // avatar dialog state
    var showAvatarDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(ui.displayName) {
        if (!editingName) {
            nameDraft = ui.displayName
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { onBack() }
                    )
                }
            )
        }
    ) { pad ->

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(pad)
        ) {

            Column(
                modifier = Modifier
                    .verticalScroll(scroll)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                if (working) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
//                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Account section
                Text("Account", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(12.dp))

                // Avatar row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showAvatarDialog = true }
                        .padding(vertical = 8.dp)
                ) {
                    if (ui.avatar != null && ui.avatar!!.isNotBlank()) {
                        AsyncImage(
                            model = ui.avatar,
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = ui.displayName.trim().firstOrNull()?.uppercase() ?: "?",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }

                    Spacer(Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text("Display name", style = MaterialTheme.typography.bodyLarge)
                        if (!editingName) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(ui.displayName.ifBlank { "Set your name" }, style = MaterialTheme.typography.bodyMedium)
                                Spacer(Modifier.width(8.dp))
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Edit",
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clickable {
                                            editingName = true
                                            nameDraft = ui.displayName
                                        }
                                )
                            }
                        } else {
                            // Edit mode: text field + save/cancel
                            OutlinedTextField(
                                value = nameDraft,
                                onValueChange = { nameDraft = it },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Row(modifier = Modifier.padding(top = 8.dp)) {
                                Button(onClick = {
                                    // Save
                                    viewModel.saveDisplayName(nameDraft)
                                    editingName = false
                                }) {
                                    Icon(Icons.Default.Done, contentDescription = null)
                                    Spacer(Modifier.width(8.dp))
                                    Text("Save")
                                }
                                Spacer(Modifier.width(8.dp))
                                OutlinedButton(onClick = {
                                    editingName = false
                                    nameDraft = ui.displayName
                                }) {
                                    Icon(Icons.Default.Close, contentDescription = null)
                                    Spacer(Modifier.width(8.dp))
                                    Text("Cancel")
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                // Personalization
                Text("Personalization", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Dark theme", style = MaterialTheme.typography.bodyLarge)
                        Text("Use dark appearance for the app", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Switch(checked = ui.themeDark, onCheckedChange = { viewModel.updateTheme(it) })
                }

                Spacer(Modifier.height(20.dp))

                // Sync & storage
                Text("Sync & storage", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Auto-sync", style = MaterialTheme.typography.bodyLarge)
                        Text("Automatically sync your data in the background", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Switch(checked = ui.autoSync, onCheckedChange = { viewModel.updateAutoSync(it) })
                }

                Spacer(Modifier.height(12.dp))

                // Storage usage row
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Storage usage", style = MaterialTheme.typography.bodyLarge)
                        Text(if (ui.storageUsage.isBlank()) "Tap refresh to calculate" else ui.storageUsage, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    TextButton(onClick = { viewModel.refreshStorageUsage() }) { Text("Refresh") }
                }

                Spacer(Modifier.height(8.dp))

                Button(onClick = { viewModel.clearCachedImages() }, modifier = Modifier.fillMaxWidth()) {
                    Text("Clear cached images")
                }

                Spacer(Modifier.height(40.dp))
            }

            // Logout button anchored to bottom
            Button(
                onClick = { showLogoutDialog = true },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(20.dp)
                    .fillMaxWidth(0.9f)
            ) {
                Text("Logout")
            }

            // Avatar dialog (simple local-only actions)
            if (showAvatarDialog) {
                AlertDialog(
                    onDismissRequest = { showAvatarDialog = false },
                    title = { Text("Avatar") },
                    text = { Text("Change or remove avatar (local only). Implement gallery picker to choose a file.") },
                    confirmButton = {
                        TextButton(onClick = {
                            // here we'd normally open a gallery picker via activity result
                            // for now we just close the dialog (UI will use viewModel.onAvatarClick to signal)
                            viewModel.onAvatarClick()
                            showAvatarDialog = false
                        }) { Text("Pick (not implemented)") }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            // remove avatar
                            viewModel.setAvatar(null)
                            showAvatarDialog = false
                        }) { Text("Remove") }
                    }
                )
            }

            // Logout confirmation
            if (showLogoutDialog) {
                AlertDialog(
                    onDismissRequest = { showLogoutDialog = false },
                    title = { Text("Logout") },
                    text = { Text("Are you sure you want to logout and clear your session?") },
                    confirmButton = {
                        TextButton(onClick = {
                            showLogoutDialog = false
                            viewModel.logout {
                                onLoggedOut()
                            }
                        }) {
                            Text("Logout")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showLogoutDialog = false }) { Text("Cancel") }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    ScreenPreview {
        SettingsScreen(
//            viewModel = SettingsViewModel(session = object : uk.ac.tees.mad.journalify.data.session.SessionManager(
//                context = androidx.compose.ui.platform.LocalContext.current.applicationContext as android.content.Context
//            ) {})
        )
    }
}
