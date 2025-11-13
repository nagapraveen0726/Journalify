package uk.ac.tees.mad.journalify.presentation.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.emptyFlow
import uk.ac.tees.mad.journalify.domain.model.JournalEntry
import uk.ac.tees.mad.journalify.presentation.components.ScreenPreview

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
            FloatingActionButton(onClick = { onCreateEntry() }) {
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
                }
            )
        }
    ) { pv ->

        Column(
            modifier = Modifier
                .padding(pv)
                .padding(horizontal = 12.dp)
        ) {

            OutlinedTextField(
                value = ui.query,
                onValueChange = viewModel::search,
                placeholder = { Text("Search...") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            if (ui.isLoading)
                CircularProgressIndicator()

            else if (ui.entries.isEmpty())
                Text("No entries yet...")

            else
                LazyColumn {
                    items(ui.entries) { e ->
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

// PREVIEW

@Preview
@Composable
fun HomeEmptyPreview() {
    ScreenPreview {
        HomeScreen(
//            viewModel = FakeHomeVM()
        )
    }
}


// fake VM for preview

//class FakeHomeVM: HomeViewModel(
//    getEntries = { emptyFlow() },
//    searchEntries = { emptyFlow() }
//)
