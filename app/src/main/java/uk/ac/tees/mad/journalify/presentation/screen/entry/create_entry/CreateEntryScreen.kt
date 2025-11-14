package uk.ac.tees.mad.journalify.presentation.screen.entry.create_entry

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import uk.ac.tees.mad.journalify.presentation.components.RichTextEditor
import uk.ac.tees.mad.journalify.presentation.components.ScreenPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEntryScreen(
    viewModel: CreateEntryViewModel = hiltViewModel(),
    onBack: () -> Unit = {},
    onSaved: () -> Unit = {}
) {
    val ui by viewModel.uiState.collectAsState()

    Scaffold(topBar = {
        TopAppBar(title = { Text("New Entry") }, navigationIcon = {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = null,
                modifier = Modifier
                    .padding(12.dp)
                    .clickable { onBack() })
        })
    }, floatingActionButton = {
        ExtendedFloatingActionButton(
            onClick = { viewModel.save(onSaved) },
            text = { Text("Save") },
            icon = { Icon(Icons.Default.ArrowBack, null) }, // is allowed? maybe use check
        )
    }) { pv ->

        Column(
            modifier = Modifier
                .padding(pv)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = ui.title,
                onValueChange = viewModel::updateTitle,
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            RichTextEditor(
                value = ui.content, onValueChange = viewModel::updateContent
            )

            Spacer(Modifier.height(16.dp))

            // Image preview
            if (ui.imagePath != null) {
                Image(
                    painter = rememberAsyncImagePainter(ui.imagePath),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }

            Spacer(Modifier.height(8.dp))

            OutlinedButton(
                onClick = {
                    // CameraX will update this in commit 12
                    viewModel.updateImage(null)
                }, modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Add, null)
                Spacer(Modifier.width(8.dp))
                Text("Add Image")
            }

            if (ui.loading) CircularProgressIndicator()
        }
    }
}


// PREVIEW
@Preview
@Composable
fun CreateEntryPreview() {
    ScreenPreview {
        CreateEntryScreen(
//            viewModel = FakeCreateVM()
        )
    }
}

//class FakeCreateVM : CreateEntryViewModel(
//    createEntry = { _, _, _ -> })
