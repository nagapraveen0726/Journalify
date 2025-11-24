package uk.ac.tees.mad.journalify.previews

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uk.ac.tees.mad.journalify.presentation.components.ScreenPreview
import uk.ac.tees.mad.journalify.presentation.screen.auth.AuthMode

@Composable
fun AuthScreen_UI(
    mode: AuthMode = AuthMode.LOGIN,
    email: String = "",
    password: String = "",
    confirmPassword: String = "",
    emailError: String? = null,
    passwordError: String? = null,
    confirmPasswordError: String? = null,
    loading: Boolean = false,
    onModeChange: (AuthMode) -> Unit = {},
    onEmailChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onConfirmPasswordChange: (String) -> Unit = {},
    onSubmitClick: () -> Unit = {}
) {
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
            AuthMode.values().forEach { m ->
                Text(
                    text = m.name,
                    color = if (mode == m) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.clickable { onModeChange(m) }
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        // EMAIL
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            isError = emailError != null,
            supportingText = {
                emailError?.let {
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
        if (mode != AuthMode.RESET) {
            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                isError = passwordError != null,
                supportingText = {
                    passwordError?.let {
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
        if (mode == AuthMode.SIGNUP) {
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = onConfirmPasswordChange,
                label = { Text("Confirm Password") },
                visualTransformation = PasswordVisualTransformation(),
                isError = confirmPasswordError != null,
                supportingText = {
                    confirmPasswordError?.let {
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
            onClick = onSubmitClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading
        ) {
            Text(
                text = when (mode) {
                    AuthMode.LOGIN -> "Login"
                    AuthMode.SIGNUP -> "Sign Up"
                    AuthMode.RESET -> "Reset Password"
                }
            )
        }

        Spacer(Modifier.height(16.dp))

        if (loading) {
            CircularProgressIndicator()
        }

        // SWITCH TO RESET PASSWORD
        if (mode == AuthMode.LOGIN) {
            Text(
                "Forgot password?",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onModeChange(AuthMode.RESET) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuthScreenPreview() {
    ScreenPreview {
        AuthScreen_UI()
    }
}
