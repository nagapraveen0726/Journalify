package uk.ac.tees.mad.journalify.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import uk.ac.tees.mad.journalify.ui.theme.JournalifyTheme

@Preview(showBackground = true)
@Composable
fun ScreenPreview(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit = {}
) {
    JournalifyTheme(darkTheme = darkTheme) {
        content()
    }
}
