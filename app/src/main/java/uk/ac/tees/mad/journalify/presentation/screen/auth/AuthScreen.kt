package uk.ac.tees.mad.journalify.presentation.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uk.ac.tees.mad.journalify.presentation.components.ScreenPreview

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = AuthViewModel(),
    onAuthSuccess: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // TOP MODE SWITCHER
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AuthMode.values().forEach { mode ->
                Text(
                    text = mode.name,
                    color = if (state.mode == mode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.clickable { viewModel.switchMode(mode) }
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        // EMAIL
        OutlinedTextField(
            value = state.email,
            onValueChange = viewModel::updateEmail,
            label = { Text("Email") },
            isError = state.emailError != null,
            supportingText = {
                state.emailError?.let {
                    Text(
                        it,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        // PASSWORD
        if (state.mode != AuthMode.RESET) {
            OutlinedTextField(
                value = state.password,
                onValueChange = viewModel::updatePassword,
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                isError = state.passwordError != null,
                supportingText = {
                    state.passwordError?.let {
                        Text(
                            it,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
        }

        // CONFIRM PASSWORD (signup only)
        if (state.mode == AuthMode.SIGNUP) {
            OutlinedTextField(
                value = state.confirmPassword,
                onValueChange = viewModel::updateConfirmPassword,
                label = { Text("Confirm Password") },
                visualTransformation = PasswordVisualTransformation(),
                isError = state.confirmPasswordError != null,
                supportingText = {
                    state.confirmPasswordError?.let {
                        Text(
                            it,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
        }

        // BUTTONS
        Button(
            onClick = {
                when (state.mode) {
                    AuthMode.LOGIN -> viewModel.submitLogin(onAuthSuccess)
                    AuthMode.SIGNUP -> viewModel.submitSignup(onAuthSuccess)
                    AuthMode.RESET -> viewModel.submitReset(onAuthSuccess)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.loading
        ) {
            Text(
                text =
                    when (state.mode) {
                        AuthMode.LOGIN -> "Login"
                        AuthMode.SIGNUP -> "Sign Up"
                        AuthMode.RESET -> "Reset Password"
                    }
            )
        }

        Spacer(Modifier.height(16.dp))

        if (state.loading) {
            CircularProgressIndicator()
        }

        // SWITCH TO RESET PASSWORD
        if (state.mode == AuthMode.LOGIN) {
            Text(
                "Forgot password?",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { viewModel.switchMode(AuthMode.RESET) }
            )
        }
    }
}

@Preview
@Composable
fun AuthScreenPreview() {
    ScreenPreview { AuthScreen() }
}

