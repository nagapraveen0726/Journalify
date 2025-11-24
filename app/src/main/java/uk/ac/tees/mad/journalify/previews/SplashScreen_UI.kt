package uk.ac.tees.mad.journalify.previews


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uk.ac.tees.mad.journalify.R
import uk.ac.tees.mad.journalify.presentation.components.ScreenPreview

@Composable
fun SplashScreen_UI(
    alpha: Float = 1f
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier
                .size(160.dp)
                .alpha(alpha)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    ScreenPreview {
        SplashScreen_UI()
    }
}