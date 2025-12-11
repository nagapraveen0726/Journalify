package uk.ac.tees.mad.journalify.presentation.screen.entry.create_entry

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
    onSaved: () -> Unit = {},
    onAddImage: () -> Unit = {},
    imagePath: String?
) {
    var showImagePicker by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            try {
                // 🔥 Take persistent permission
                context.contentResolver.takePersistableUriPermission(
                    it,
                    android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                Log.d("CreateEntryScreen", "Persistent permission granted for: $it")
            } catch (e: SecurityException) {
                Log.e("CreateEntryScreen", "Failed to take persistent permission", e)
            }

            viewModel.updateImage(it.toString())
        }
    }

    val ui by viewModel.uiState.collectAsState()

    LaunchedEffect(imagePath) {
        imagePath?.let { viewModel.updateImage(it) }
    }

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
                onClick = { viewModel.save(onSaved) },
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
                value = ui.title,
                onValueChange = viewModel::updateTitle,
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            RichTextEditor(
                value = ui.content,
                onValueChange = viewModel::updateContent
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Formatting rules",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(16.dp))

            Text(
                text = "• Bold: Wrap text with ** at the start and end\n  Example: **this is bold**",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(16.dp))

            Text(
                text = "• Italic: Wrap text with _ at the start and end\n  Example: _this is italic_",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(16.dp))
            Text(
                text = "• Underline: Wrap text with __ at the start and end\n  Example: __this is underlined__",
                style = MaterialTheme.typography.bodyMedium
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
                onClick = { showImagePicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Add, null)
                Spacer(Modifier.width(8.dp))
                Text("Add Image")
            }

            if (showImagePicker) {
                AlertDialog(
                    onDismissRequest = { showImagePicker = false },
                    title = { Text("Add image") },
                    text = { Text("Choose image source") },
                    confirmButton = {
                        TextButton(onClick = {
                            showImagePicker = false
                            onAddImage() // 📸 Camera
                        }) {
                            Text("Camera")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            showImagePicker = false
                            galleryLauncher.launch("image/*") // 🖼️ Gallery
                        }) {
                            Text("Gallery")
                        }
                    }
                )
            }
        }

        if (ui.loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}