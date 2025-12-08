package uk.ac.tees.mad.journalify.presentation.previews

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEntryScreen_UI(
    title: String = "",
    content: String = "",
    imagePath: String? = null,
    onBack: () -> Unit = {}
) {

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
                value = title,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Title") }
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = content,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Content") },
                minLines = 8
            )

            Spacer(Modifier.height(16.dp))

            imagePath?.let {
                AsyncImage(
                    model = it,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )

                Spacer(Modifier.height(16.dp))
            }

            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Changes")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Preview_EditEntryScreen_UI() {
    EditEntryScreen_UI(
        title = "Sample title",
        content = "Sample long content goes here…",
        imagePath = null
    )
}
