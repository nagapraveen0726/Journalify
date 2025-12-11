package uk.ac.tees.mad.journalify.presentation.screen.entry.detail_entry

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import formatText
import uk.ac.tees.mad.journalify.util.formatTime
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailEntryScreen(
    id: String,
    viewModel: DetailEntryViewModel = hiltViewModel(),
    onBack: () -> Unit = {},
    onEdit: (String) -> Unit = {}
) {
    LaunchedEffect(id) { viewModel.load(id) }

    val ui by viewModel.state.collectAsState()
    val ctx = LocalContext.current
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
                            .clickable(onClick = onBack)
                    )
                },
                actions = {
                    Icon(
                        Icons.Default.Edit,
                        null,
                        modifier = Modifier
                            .padding(end = 14.dp)
                            .clickable { onEdit(id) }
                    )
                    Icon(
                        Icons.Default.Share,
                        null,
                        modifier = Modifier
                            .padding(end = 14.dp)
                            .clickable {
                                val e = ui.entry ?: return@clickable
                                val share = Intent(Intent.ACTION_SEND)
                                share.type = "text/plain"
                                share.putExtra(Intent.EXTRA_TEXT, e.content)
                                ctx.startActivity(
                                    Intent.createChooser(share, "Share Entry")
                                )
                            }
                    )
                    Icon(
                        Icons.Default.Delete,
                        null,
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable { viewModel.delete(onBack) }
                    )
                }
            )
        }
    ) { pad ->

        ui.entry?.let { entry ->

            val formattedTime = remember(entry.createdAt) {
                SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
                    .format(Date(entry.createdAt))
            }

            Column(
                modifier = Modifier
                    .padding(pad)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(scroll)
            ) {

                Spacer(Modifier.height(20.dp))

                Text(
                    entry.title,
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(Modifier.height(10.dp))
                Divider()
                Spacer(Modifier.height(12.dp))

                // 👇 cloud OR local fallback
                (entry.imageUrl ?: entry.imagePath)?.let { image ->

                    AsyncImage(
                        model = image,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                    )

                    Spacer(Modifier.height(18.dp))
                }

                val content= formatText(entry.content)
                Text(
                    content,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(Modifier.height(40.dp))
                Divider()
                Spacer(Modifier.height(12.dp))

                Text(
                    "Created: ${formatTime(entry.createdAt)}",
                    style = MaterialTheme.typography.bodySmall
                )


                Spacer(Modifier.height(40.dp))
            }
        }
    }
}
