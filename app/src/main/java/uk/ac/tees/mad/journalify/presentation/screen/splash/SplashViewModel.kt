package uk.ac.tees.mad.journalify.presentation.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import uk.ac.tees.mad.journalify.data.session.SessionManager
import uk.ac.tees.mad.journalify.util.ConnectivityObserver
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val sessionManager: SessionManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SplashState>(SplashState.Loading)
    val uiState: StateFlow<SplashState> = _uiState

    init { performChecks() }

    private fun performChecks() {
        viewModelScope.launch {

            delay(2000) // 2 sec fade splash

            val currentUser = auth.currentUser
            val isLoggedIn = sessionManager.isLoggedIn.first()

            if (currentUser != null && isLoggedIn) {
                _uiState.value = SplashState.NavigateHome
            } else {
                _uiState.value = SplashState.NavigateAuth
            }
        }
    }
}
