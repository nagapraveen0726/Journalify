package uk.ac.tees.mad.journalify.previews

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import uk.ac.tees.mad.journalify.domain.model.JournalEntry
import uk.ac.tees.mad.journalify.presentation.components.ScreenPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen_UI(
    query: String = "",
    isLoading: Boolean = false,
    entries: List<JournalEntry> = emptyList(),
    onQueryChange: (String) -> Unit = {},
    onCreateEntry: () -> Unit = {},
    onOpenEntry: (String) -> Unit = {},
    onOpenSettings: () -> Unit = {},
    onSync: () -> Unit = {}
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateEntry) {
                Icon(Icons.Default.Add, null)
            }
        },
        topBar = {
            TopAppBar(
                title = { Text("Journalify") },
                actions = {
                    Text(
                        "Settings",
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clickable { onOpenSettings() }
                    )
                    Text(
                        "Sync",
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clickable { onSync() }
                    )
                }
            )
        },
    ) { pv ->

        Column(
            modifier = Modifier
                .padding(pv)
                .padding(horizontal = 12.dp)
        ) {

            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = { Text("Search...") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            when {
                isLoading -> CircularProgressIndicator()

                entries.isEmpty() -> Text("No entries yet...")

                else -> LazyColumn {
                    items(entries) { e ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onOpenEntry(e.id) }
                                .padding(10.dp)
                        ) {
                            Text(e.title, style = MaterialTheme.typography.titleMedium)
                            Text(e.content.take(40) + "...", maxLines = 1)
                            Spacer(Modifier.height(10.dp))
                        }
                        Divider()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeEmptyPreview() {
    val fakeEntries = listOf(
        JournalEntry(id = "1", title = "First Entry", content = "Today was great!", imagePath = null, createdAt = 0, updatedAt = 0, isSynced = false),
        JournalEntry(id = "2", title = "Second Entry", content = "I wrote some code.", imagePath = null, createdAt = 0, updatedAt = 0, isSynced = false),
    )

    ScreenPreview {
        HomeScreen_UI(
            query = "",
            isLoading = false,
            entries = fakeEntries
        )
    }
}