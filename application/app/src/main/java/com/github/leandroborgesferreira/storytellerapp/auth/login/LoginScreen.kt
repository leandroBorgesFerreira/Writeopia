package com.github.leandroborgesferreira.storytellerapp.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun LoginScreenBinding(loginViewModel: LoginViewModel) {
    LoginScreen(
        emailState = loginViewModel.email,
        passwordState = loginViewModel.password,
        emailChanged = loginViewModel::emailChanged,
        passwordChanged = loginViewModel::passwordChanged,
        onLogin = {}
    )
}

@Composable
fun LoginScreen(
    emailState: StateFlow<String>,
    passwordState: StateFlow<String>,
    emailChanged: (String) -> Unit,
    passwordChanged: (String) -> Unit,
    onLogin: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val email by emailState.collectAsStateWithLifecycle()
        val password by passwordState.collectAsStateWithLifecycle()

        Column(
            modifier = Modifier
                .padding(horizontal = 50.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = emailChanged,
                placeholder = {
                    Text(text = "Email")
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = passwordChanged,
                placeholder = {
                    Text(text = "Password")
                }
            )

            Spacer(modifier = Modifier.height(8.dp))


            TextButton(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .fillMaxWidth(),
                onClick = onLogin
            ) {
                Text(text = "Register", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        emailState = MutableStateFlow(""),
        passwordState = MutableStateFlow(""),
        emailChanged = { },
        passwordChanged = { },
        onLogin = { },
    )
}
