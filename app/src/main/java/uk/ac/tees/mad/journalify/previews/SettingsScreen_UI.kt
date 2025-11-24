package uk.ac.tees.mad.journalify.previews

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import uk.ac.tees.mad.journalify.presentation.components.ScreenPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen_UI(
    displayName: String = "John Doe",
    themeDark: Boolean = true,
    autoSync: Boolean = true,
    onDisplayNameChange: (String) -> Unit = {},
    onThemeChange: (Boolean) -> Unit = {},
    onAutoSyncChange: (Boolean) -> Unit = {},
    onBack: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(12.dp)
                            .clickable(onClick = onBack)
                    )
                }
            )
        }
    ) { pv ->

        Column(
            modifier = Modifier
                .padding(pv)
                .padding(20.dp)
        ) {

            Text("Display name")
            OutlinedTextField(
                value = displayName,
                onValueChange = onDisplayNameChange,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Dark Theme")
                Spacer(Modifier.weight(1f))
                Switch(
                    checked = themeDark,
                    onCheckedChange = onThemeChange
                )
            }

            Spacer(Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Auto-Sync")
                Spacer(Modifier.weight(1f))
                Switch(
                    checked = autoSync,
                    onCheckedChange = onAutoSyncChange
                )
            }

            Spacer(Modifier.height(40.dp))

            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Logout")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    ScreenPreview {
        SettingsScreen_UI()
    }
}