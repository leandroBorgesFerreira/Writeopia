package io.writeopia.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.writeopia.common.utils.ResultData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import io.writeopia.appresourcers.R

@Composable
internal fun LoginScreenBinding(loginViewModel: LoginViewModel, onLoginSuccess: () -> Unit) {
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
private fun LoginScreen(
    emailState: StateFlow<String>,
    passwordState: StateFlow<String>,
    loginState: StateFlow<ResultData<Boolean>>,
    emailChanged: (String) -> Unit,
    passwordChanged: (String) -> Unit,
    onLoginRequest: () -> Unit,
    onLoginSuccess: () -> Unit,
) {
    val login = loginState.collectAsStateWithLifecycle().value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (login) {
            is ResultData.Complete -> {
                if (login.data) {
                    LaunchedEffect(key1 = "navigation") {
                        onLoginSuccess()
                    }
                } else {
                    LoginContent(
                        emailState,
                        passwordState,
                        emailChanged,
                        passwordChanged,
                        onLoginRequest
                    )
                }
            }

            is ResultData.Idle, is ResultData.Error -> {
                LoginContent(
                    emailState,
                    passwordState,
                    emailChanged,
                    passwordChanged,
                    onLoginRequest
                )
            }

            is ResultData.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
private fun BoxScope.LoginContent(
    emailState: StateFlow<String>,
    passwordState: StateFlow<String>,
    emailChanged: (String) -> Unit,
    passwordChanged: (String) -> Unit,
    onLoginRequest: () -> Unit,
) {
    val email by emailState.collectAsStateWithLifecycle()
    val password by passwordState.collectAsStateWithLifecycle()
    var showPassword by remember { mutableStateOf(false) }

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
                Text(text = stringResource(id = R.string.email))
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = passwordChanged,
            placeholder = {
                Text(text = stringResource(id = R.string.password))
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
                        contentDescription = stringResource(
                            R.string.content_description_visibility_off
                        )
                    )
                } else {
                    Icon(
                        modifier = Modifier.clickable {
                            showPassword = !showPassword
                        },
                        imageVector = Icons.Default.Visibility,
                        contentDescription = stringResource(
                            R.string.content_description_visibility_on
                        )
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
            Text(
                text = stringResource(id = R.string.enter),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
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
