package uk.ac.tees.mad.journalify.presentation.screen.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import uk.ac.tees.mad.journalify.R
import uk.ac.tees.mad.journalify.presentation.components.ScreenPreview

@Composable
fun SplashScreen(
    viewModel: SplashViewModel? = null,
    goToAuth: () -> Unit = {},
    goToHome: () -> Unit = {}
) {
    val state: SplashState by (
            viewModel?.uiState?.collectAsState()
                ?: remember { mutableStateOf(SplashState.Loading) }
            )

    // Anim fade-in
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(1400)
    )

    // Trigger navigation when state changes
    LaunchedEffect(state) {
        when (state) {
            is SplashState.NavigateAuth -> goToAuth()
            is SplashState.NavigateHome -> goToHome()
            else -> Unit
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier.size(160.dp).alpha(alpha)
        )
    }
}

@Composable
@Preview
fun SplashScreenPreview() {
    ScreenPreview {
        SplashScreen()
    }
}
