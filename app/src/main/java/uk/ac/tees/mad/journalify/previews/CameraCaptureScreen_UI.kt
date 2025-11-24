package uk.ac.tees.mad.journalify.previews

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uk.ac.tees.mad.journalify.presentation.components.ScreenPreview

@Composable
fun CameraCaptureScreen_UI() {
    Box(modifier = Modifier.fillMaxSize()) {
        // Empty box as fake preview background

        FloatingActionButton(
            onClick = { /* no-op in preview */ },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
        ) {
            Icon(Icons.Default.Add, null)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CameraCaptureScreenPreview() {
    ScreenPreview {
        CameraCaptureScreen_UI()
    }
}