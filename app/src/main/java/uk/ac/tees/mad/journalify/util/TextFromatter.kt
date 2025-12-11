import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun FormatterTest() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Testing Markdown Formatter:", style = MaterialTheme.typography.headlineSmall)

        Divider()

        TestCase("Bold", "This is **bold text** here")
        TestCase("Italic", "This is _italic text_ here")
        TestCase("Underline", "This is __underlined text__ here")
        TestCase("Mixed", "**Bold** and _italic_ and __underline__")
        TestCase("Nested", "**Bold _and italic_** together")
        TestCase("Multiple", "**First bold** then _first italic_ and **second bold**")
    }
}

@Composable
fun TestCase(label: String, input: String) {
    Column {
        Text("$label:", style = MaterialTheme.typography.labelMedium)
        Text(text = formatText(input))
        Text("Raw: $input", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
        Divider()
    }
}

fun formatText(input: String): AnnotatedString {
    return buildAnnotatedString {
        var i = 0

        while (i < input.length) {
            when {
                // Check for ** (bold) - must come before single *
                i + 1 < input.length && input[i] == '*' && input[i + 1] == '*' -> {
                    val end = findNext(input, "**", i + 2)
                    if (end != -1) {
                        pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                        append(input.substring(i + 2, end))
                        pop()
                        i = end + 2
                    } else {
                        append("**")
                        i += 2
                    }
                }

                // Check for __ (underline) - must come before single _
                i + 1 < input.length && input[i] == '_' && input[i + 1] == '_' -> {
                    val end = findNext(input, "__", i + 2)
                    if (end != -1) {
                        pushStyle(SpanStyle(textDecoration = TextDecoration.Underline))
                        append(input.substring(i + 2, end))
                        pop()
                        i = end + 2
                    } else {
                        append("__")
                        i += 2
                    }
                }

                // Check for _ (italic)
                input[i] == '_' -> {
                    val end = findNext(input, "_", i + 1)
                    if (end != -1) {
                        pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
                        append(input.substring(i + 1, end))
                        pop()
                        i = end + 1
                    } else {
                        append("_")
                        i++
                    }
                }

                // Check for * (italic)
                input[i] == '*' -> {
                    val end = findNext(input, "*", i + 1)
                    if (end != -1) {
                        pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
                        append(input.substring(i + 1, end))
                        pop()
                        i = end + 1
                    } else {
                        append("*")
                        i++
                    }
                }

                else -> {
                    append(input[i])
                    i++
                }
            }
        }
    }
}

private fun findNext(input: String, delimiter: String, start: Int): Int {
    val index = input.indexOf(delimiter, start)
    return if (index != -1) index else -1
}

@Preview(showBackground = true)
@Composable
fun pre(){
    FormatterTest()
}