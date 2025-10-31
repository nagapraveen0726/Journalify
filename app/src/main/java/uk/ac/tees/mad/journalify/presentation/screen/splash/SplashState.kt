package uk.ac.tees.mad.journalify.presentation.screen.splash

sealed class SplashState {
    object Loading : SplashState()
    object NavigateAuth : SplashState()
    object NavigateHome : SplashState()
}