package com.github.leandroborgesferreira.storytellerapp.auth.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun RegisterScreenBinding(registerViewModel: RegisterViewModel) {
    LaunchedEffect(key1 = "registerViewModel", block = {
        registerViewModel.init()
    })

    RegisterScreen(
        nameState = registerViewModel.name,
        emailState = registerViewModel.email,
        passwordState = registerViewModel.password,
        nameChanged = registerViewModel::nameChanged,
        emailChanged = registerViewModel::emailChanged,
        passwordChanged = registerViewModel::passwordChanged,
        onRegister = registerViewModel::onRegister
    )
}

@Composable
fun RegisterScreen(
    nameState: StateFlow<String>,
    emailState: StateFlow<String>,
    passwordState: StateFlow<String>,
    nameChanged: (String) -> Unit,
    emailChanged: (String) -> Unit,
    passwordChanged: (String) -> Unit,
    onRegister: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val name by nameState.collectAsStateWithLifecycle()
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
                value = name,
                onValueChange = nameChanged,
                singleLine = true,
                placeholder = {
                    Text(text = "Name")
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = emailChanged,
                singleLine = true,
                placeholder = {
                    Text(text = "Email")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = passwordChanged,
                singleLine = true,
                placeholder = {
                    Text(text = "Password")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
            )

            Spacer(modifier = Modifier.height(8.dp))


            TextButton(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .fillMaxWidth(),
                onClick = onRegister
            ) {
                Text(text = "Register", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

@Preview
@Composable
fun AuthScreenPreview() {
    RegisterScreen(
        nameState = MutableStateFlow(""),
        emailState = MutableStateFlow(""),
        passwordState = MutableStateFlow(""),
        nameChanged = {},
        emailChanged = {},
        passwordChanged = {},
        onRegister = {}
    )
}
