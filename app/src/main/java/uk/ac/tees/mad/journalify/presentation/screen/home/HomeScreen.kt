package uk.ac.tees.mad.journalify.presentation.screen.home

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import formatText
import uk.ac.tees.mad.journalify.domain.model.JournalEntry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onCreateEntry: () -> Unit = {},
    onOpenEntry: (String) -> Unit = {},
    onOpenSettings: () -> Unit = {}
) {
    val ui by viewModel.uiState.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateEntry) {
                Icon(Icons.Default.Add, contentDescription = "New entry")
            }
        },
        topBar = {
            TopAppBar(
                title = { Text("Journalify") },
                actions = {
                    TopBarAction("Settings", onOpenSettings)
                    TopBarAction("Sync") { viewModel.syncFromCloud() }
                    TopBarAction("Upload") { viewModel.syncToCloud() }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {

            OutlinedTextField(
                value = ui.query,
                onValueChange = viewModel::search,
                placeholder = { Text("Search entries") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(16.dp))

            when {
                ui.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                ui.entries.isEmpty() -> {
                    Text(
                        text = "No entries yet",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 24.dp)
                    )
                }

                else -> {
                    LazyColumn {
                        items(ui.entries) { entry ->
                            EntryRow(
                                entry = entry,
                                onClick = { onOpenEntry(entry.id) }
                            )
                            Divider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TopBarAction(
    label: String,
    onClick: () -> Unit
) {
    Text(
        text = label,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .clickable(onClick = onClick)
    )
}

@Composable
private fun EntryRow(
    entry: JournalEntry,
    onClick: () -> Unit
) {
    val previewText = formatText(entry.content)
    val image = entry.imagePath ?: entry.imageUrl

    // 🔍 DEBUG: Log the values
    Log.d("EntryRow", "Entry: ${entry.id}")
    Log.d("EntryRow", "imagePath: ${entry.imagePath}")
    Log.d("EntryRow", "imageUrl: ${entry.imageUrl}")
    Log.d("EntryRow", "Final image: $image")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (!image.isNullOrBlank()) {
            AsyncImage(
                model = image,
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .clip(MaterialTheme.shapes.medium)
            )
            Spacer(Modifier.width(12.dp))
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = entry.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = previewText,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}