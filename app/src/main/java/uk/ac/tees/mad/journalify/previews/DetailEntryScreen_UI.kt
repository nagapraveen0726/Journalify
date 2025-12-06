package uk.ac.tees.mad.journalify.previews

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import uk.ac.tees.mad.journalify.util.formatTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailEntryScreen_UI(
    title: String = "",
    content: String = "",
    image: String? = null,
    createdAt: Long = 0L,
    onBack: () -> Unit = {},
    onEdit: () -> Unit = {},
    onShare: () -> Unit = {},
    onDelete: () -> Unit = {}
) {

    val scroll = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Journal Entry") },
                navigationIcon = {
                    Icon(
                        Icons.Default.ArrowBack,
                        null,
                        modifier = Modifier
                            .padding(12.dp)
                            .clickable { onBack() }
                    )
                },
                actions = {
                    Icon(
                        Icons.Default.Edit, null,
                        modifier = Modifier
                            .padding(end = 14.dp)
                            .clickable { onEdit() }
                    )
                    Icon(
                        Icons.Default.Share, null,
                        modifier = Modifier
                            .padding(end = 14.dp)
                            .clickable { onShare() }
                    )
                    Icon(
                        Icons.Default.Delete, null,
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable { onDelete() }
                    )
                }
            )
        }
    ) { pad ->

        Column(
            modifier = Modifier
                .padding(pad)
                .padding(horizontal = 20.dp)
                .verticalScroll(scroll)
        ) {
            Spacer(Modifier.height(20.dp))

            Text(title, style = MaterialTheme.typography.headlineSmall)

            Spacer(Modifier.height(10.dp))
            Divider()
            Spacer(Modifier.height(12.dp))

            image?.let {
                AsyncImage(
                    model = it,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                )
                Spacer(Modifier.height(18.dp))
            }

            Text(content, style = MaterialTheme.typography.bodyLarge)

            Spacer(Modifier.height(40.dp))
            Divider()
            Spacer(Modifier.height(12.dp))

            Text(
                "Created: ${formatTime(createdAt)}",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(Modifier.height(40.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview_DetailEntryScreen_UI() {
    DetailEntryScreen_UI(
        title = "A day in York",
        content = "Went hiking and took pictures!",
        image = null,
        createdAt = System.currentTimeMillis()
    )
}
