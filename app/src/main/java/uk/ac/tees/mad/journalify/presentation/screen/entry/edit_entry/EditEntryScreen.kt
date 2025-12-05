package uk.ac.tees.mad.journalify.presentation.screen.entry.edit_entry

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEntryScreen(
    id: String,
    viewModel: EditEntryViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    LaunchedEffect(id) {
        viewModel.load(id)
    }

    val ui by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Entry") },
                navigationIcon = {
                    Icon(
                        Icons.Default.ArrowBack,
                        null,
                        modifier = Modifier
                            .padding(12.dp)
                            .clickable { onBack() }
                    )
                }
            )
        }
    ) { pad ->

        Column(
            Modifier
                .padding(pad)
                .padding(20.dp)
        ) {

            OutlinedTextField(
                value = ui.title,
                onValueChange = viewModel::updateTitle,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Title") }
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = ui.content,
                onValueChange = viewModel::updateContent,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Content") },
                minLines = 8
            )

            Spacer(Modifier.height(16.dp))

            ui.imagePath?.let { p ->
                AsyncImage(
                    model = p,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )

                Spacer(Modifier.height(16.dp))
            }

            Button(
                onClick = { viewModel.save(onBack) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Changes")
            }
        }
    }
}
