package uk.ac.tees.mad.journalify.presentation.previews

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
import coil.compose.rememberAsyncImagePainter
import uk.ac.tees.mad.journalify.presentation.components.RichTextEditor
import uk.ac.tees.mad.journalify.presentation.components.ScreenPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEntryScreen_UI(
    title: String = "",
    content: String = "",
    imagePath: String? = null,
    loading: Boolean = false,
    onTitleChange: (String) -> Unit = {},
    onContentChange: (String) -> Unit = {},
    onImageChange: (String?) -> Unit = {},
    onBack: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    onAddImageClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Entry") },
                navigationIcon = {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(12.dp)
                            .clickable { onBack() }
                    )
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onSaveClick,
                text = { Text("Save") },
                icon = { Icon(Icons.Default.ArrowBack, null) },
            )
        }
    ) { pv ->

        Column(
            modifier = Modifier
                .padding(pv)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = onTitleChange,
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            RichTextEditor(
                value = content,
                onValueChange = onContentChange
            )

            Spacer(Modifier.height(16.dp))

            // Image preview
            if (imagePath != null) {
                Image(
                    painter = rememberAsyncImagePainter(imagePath),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }

            Spacer(Modifier.height(8.dp))

            OutlinedButton(
                onClick = {
                    onImageChange(null)
                    onAddImageClick()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Add, null)
                Spacer(Modifier.width(8.dp))
                Text("Add Image")
            }

            if (loading) {
                Spacer(Modifier.height(12.dp))
                CircularProgressIndicator()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateEntryPreview() {
    ScreenPreview {
        CreateEntryScreen_UI(
            title = "My day",
            content = "Today I wrote a lot of code.",
            imagePath = null
        )
    }
}