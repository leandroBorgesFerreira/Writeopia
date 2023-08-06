package com.github.leandroborgesferreira.storytellerapp.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.leandroborgesferreira.storytellerapp.utils_module.ResultData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun LoginScreenBinding(loginViewModel: LoginViewModel, onLoginSuccess: () -> Unit) {
    loginViewModel.init()

    LoginScreen(
        emailState = loginViewModel.email,
        passwordState = loginViewModel.password,
        loginState = loginViewModel.loginState,
        emailChanged = loginViewModel::emailChanged,
        passwordChanged = loginViewModel::passwordChanged,
        onLoginRequest = loginViewModel::onLoginRequest,
        onLoginSuccess = onLoginSuccess,
    )
}

@Composable
fun LoginScreen(
    emailState: StateFlow<String>,
    passwordState: StateFlow<String>,
    loginState: StateFlow<ResultData<Unit>>,
    emailChanged: (String) -> Unit,
    passwordChanged: (String) -> Unit,
    onLoginRequest: () -> Unit,
    onLoginSuccess: () -> Unit,
) {
    val email by emailState.collectAsStateWithLifecycle()
    val password by passwordState.collectAsStateWithLifecycle()
    val login by loginState.collectAsStateWithLifecycle()
    var showPassword by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (login) {
            is ResultData.Complete -> {
                LaunchedEffect(key1 = "navigation") {
                    onLoginSuccess()
                }
            }

            is ResultData.Idle, is ResultData.Error -> {
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
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = password,
                        onValueChange = passwordChanged,
                        placeholder = {
                            Text(text = "Password")
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = if (showPassword) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        trailingIcon = {
                            if (showPassword) {
                                Icon(
                                    modifier = Modifier.clickable {
                                        showPassword = !showPassword
                                    },
                                    imageVector = Icons.Default.VisibilityOff,
                                    contentDescription = ""
                                )
                            } else {
                                Icon(
                                    modifier = Modifier.clickable {
                                        showPassword = !showPassword
                                    },
                                    imageVector = Icons.Default.Visibility,
                                    contentDescription = ""
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextButton(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primary)
                            .fillMaxWidth(),
                        onClick = onLoginRequest
                    ) {
                        Text(text = "Enter", color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }

            is ResultData.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
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
        loginState = MutableStateFlow(ResultData.Idle()),
        emailChanged = { },
        passwordChanged = { },
        onLoginRequest = { },
        onLoginSuccess = {},
    )
}
