package uk.ac.tees.mad.journalify.presentation.previews

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import uk.ac.tees.mad.journalify.presentation.components.ScreenPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen_UI() {

    val scroll = rememberScrollState()

    // Fake static UI data
    var avatar = remember { "" } // empty → fallback initial
    var displayName by remember { mutableStateOf("Alex Johnson") }
    var editingName by remember { mutableStateOf(false) }
    var nameDraft by remember { mutableStateOf(displayName) }

    var darkTheme by remember { mutableStateOf(false) }
    var autoSync by remember { mutableStateOf(true) }
    var storageUsage by remember { mutableStateOf("42 MB") }

    var showAvatarDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.padding(8.dp)
                    )
                }
            )
        }
    ) { pad ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(pad)
        ) {

            Column(
                modifier = Modifier
                    .verticalScroll(scroll)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {

                // SECTION — Account
                Text(
                    "Account",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(12.dp))

                // Avatar row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showAvatarDialog = true }
                        .padding(vertical = 8.dp)
                ) {
                    if (avatar.isNotBlank()) {
                        AsyncImage(
                            model = avatar,
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
                                text = displayName.first().uppercase(),
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }

                    Spacer(Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text("Display name", style = MaterialTheme.typography.bodyLarge)

                        if (!editingName) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    displayName.ifBlank { "Set your name" },
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(Modifier.width(8.dp))
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Edit",
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clickable {
                                            editingName = true
                                            nameDraft = displayName
                                        }
                                )
                            }
                        } else {
                            OutlinedTextField(
                                value = nameDraft,
                                onValueChange = { nameDraft = it },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Row(modifier = Modifier.padding(top = 8.dp)) {
                                Button(onClick = {
                                    displayName = nameDraft
                                    editingName = false
                                }) {
                                    Icon(Icons.Default.Done, contentDescription = null)
                                    Spacer(Modifier.width(8.dp))
                                    Text("Save")
                                }
                                Spacer(Modifier.width(8.dp))
                                OutlinedButton(onClick = {
                                    editingName = false
                                    nameDraft = displayName
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

                // SECTION — Personalization
                Text(
                    "Personalization",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Dark theme")
                        Text(
                            "Use dark appearance for the app",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(checked = darkTheme, onCheckedChange = { darkTheme = it })
                }

                Spacer(Modifier.height(20.dp))

                // SECTION — Sync & Storage
                Text(
                    "Sync & storage",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Auto-sync")
                        Text(
                            "Automatically sync your data in the background",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(checked = autoSync, onCheckedChange = { autoSync = it })
                }

                Spacer(Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Storage usage")
                        Text(
                            storageUsage,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    TextButton(onClick = { storageUsage = "42 MB" }) {
                        Text("Refresh")
                    }
                }

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Clear cached images")
                }

                Spacer(Modifier.height(80.dp))
            }

            // Logout button bottom anchored
            Button(
                onClick = { showLogoutDialog = true },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(20.dp)
                    .fillMaxWidth(0.9f)
            ) {
                Text("Logout")
            }

            // Avatar dialog (preview-safe)
            if (showAvatarDialog) {
                AlertDialog(
                    onDismissRequest = { showAvatarDialog = false },
                    title = { Text("Avatar") },
                    text = { Text("This is a preview dialog. No real actions here.") },
                    confirmButton = {
                        TextButton(onClick = { showAvatarDialog = false }) {
                            Text("OK")
                        }
                    }
                )
            }

            // Logout dialog (preview-safe)
            if (showLogoutDialog) {
                AlertDialog(
                    onDismissRequest = { showLogoutDialog = false },
                    title = { Text("Logout") },
                    text = { Text("This is a preview confirmation.") },
                    confirmButton = {
                        TextButton(onClick = { showLogoutDialog = false }) {
                            Text("Logout")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showLogoutDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview_SettingsScreen_UI() {
    ScreenPreview {
        SettingsScreen_UI()
    }
}
