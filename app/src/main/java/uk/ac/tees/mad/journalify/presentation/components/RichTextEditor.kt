package uk.ac.tees.mad.journalify.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RichTextEditor(
    value: String,
    onValueChange: (String) -> Unit
) {
    Column {

        // Placeholder toolbar (bold, italic, underline)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AssistChip(onClick = {}, label = { Text("Bold") })
            AssistChip(onClick = {}, label = { Text("Italic") })
            AssistChip(onClick = {}, label = { Text("Underline") })
        }
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 150.dp),
            placeholder = { Text("Write your thoughts...") }
        )
    }
}
