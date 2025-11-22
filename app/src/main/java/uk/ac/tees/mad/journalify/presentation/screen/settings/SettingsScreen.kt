package uk.ac.tees.mad.journalify.presentation.screen.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    Icon(
                        Icons.Default.ArrowBack,
                        null,
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
                value = ui.displayName,
                onValueChange = viewModel::updateDisplayName,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))


            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Dark Theme")
                Spacer(Modifier.weight(1f))
                Switch(
                    checked = ui.themeDark,
                    onCheckedChange = viewModel::updateTheme
                )
            }

            Spacer(Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Auto-Sync")
                Spacer(Modifier.weight(1f))
                Switch(
                    checked = ui.autoSync,
                    onCheckedChange = viewModel::updateAutoSync
                )
            }

            Spacer(Modifier.height(40.dp))

            Button(
                onClick = { viewModel.logout(onLoggedOut) },
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


@Composable
@Preview(showBackground = true)
fun SettingsScreenPreview() {
    ScreenPreview {
        SettingsScreen()
    }
}
