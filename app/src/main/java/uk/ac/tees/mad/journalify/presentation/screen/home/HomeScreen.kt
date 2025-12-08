package uk.ac.tees.mad.journalify.presentation.screen.home

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

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
                    Text(
                        "Sync",
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clickable { viewModel.syncFromCloud() }
                    )
                    Text(
                        "Upload",
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clickable { viewModel.syncToCloud() }
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
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onOpenEntry(e.id) }
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            val img = e.imageUrl ?: e.imagePath
                            if (!img.isNullOrBlank()) {
                                AsyncImage(
                                    model = img,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(MaterialTheme.shapes.medium)
                                )

                                Spacer(Modifier.width(12.dp))
                            }

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    e.title,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = e.content.take(40) + "…",
                                    maxLines = 1,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                        Divider()
                    }
                }
        }
    }
}
