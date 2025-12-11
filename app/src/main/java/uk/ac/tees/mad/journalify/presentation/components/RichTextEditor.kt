package uk.ac.tees.mad.journalify.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import formatText

@Composable
fun RichTextEditor(
    value: String,
    onValueChange: (String) -> Unit
) {
    Column {

        // Placeholder toolbar (bold, italic, underline)
//        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
//            AssistChip(onClick = { onValueChange("${value}") }, label = { Text("Bold") })
//            AssistChip(onClick = { onValueChange("${value}_italic_") }, label = { Text("Italic") })
//            AssistChip(onClick = { onValueChange("${value}__underline__") }, label = { Text("Underline") })
//        }
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 150.dp),
            placeholder = { Text("Write your thoughts...") }
        )

        Spacer(Modifier.height(12.dp))

        //
        Text(
            text = "Text Preview:- ",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(formatText(value))
    }
}
