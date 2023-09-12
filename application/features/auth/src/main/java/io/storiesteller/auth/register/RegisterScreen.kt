package io.storiesteller.auth.register

import android.util.Log
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
import io.storiesteller.utils_module.ResultData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import io.storiesteller.appresourcers.R

@Composable
fun RegisterScreen(
    nameState: StateFlow<String>,
    emailState: StateFlow<String>,
    passwordState: StateFlow<String>,
    registerState: StateFlow<ResultData<Boolean>>,
    nameChanged: (String) -> Unit,
    emailChanged: (String) -> Unit,
    passwordChanged: (String) -> Unit,
    onRegisterRequest: () -> Unit,
    onRegisterSuccess: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (val register = registerState.collectAsStateWithLifecycle().value) {
            is ResultData.Complete -> {
                if (register.data) {
                    LaunchedEffect(key1 = "navigation") {
                        Log.d("RegisterScreen", "onRegisterSuccess()")
                        onRegisterSuccess()
                    }
                } else {
                    RegisterContent(
                        nameState,
                        emailState,
                        passwordState,
                        nameChanged,
                        emailChanged,
                        passwordChanged,
                        onRegisterRequest
                    )
                }
            }

            is ResultData.Idle, is ResultData.Error -> {
                RegisterContent(
                    nameState,
                    emailState,
                    passwordState,
                    nameChanged,
                    emailChanged,
                    passwordChanged,
                    onRegisterRequest
                )
            }

            is ResultData.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
private fun BoxScope.RegisterContent(
    nameState: StateFlow<String>,
    emailState: StateFlow<String>,
    passwordState: StateFlow<String>,
    nameChanged: (String) -> Unit,
    emailChanged: (String) -> Unit,
    passwordChanged: (String) -> Unit,
    onRegisterRequest: () -> Unit,
) {
    val name by nameState.collectAsStateWithLifecycle()
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
            value = name,
            onValueChange = nameChanged,
            singleLine = true,
            placeholder = {
                Text(text = stringResource(id = R.string.name))
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = emailChanged,
            singleLine = true,
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
            singleLine = true,
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
            onClick = onRegisterRequest
        ) {
            Text(
                text = stringResource(id = R.string.register),
                color = MaterialTheme.colorScheme.onPrimary
            )
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
        registerState = MutableStateFlow(ResultData.Idle()),
        nameChanged = {},
        emailChanged = {},
        passwordChanged = {},
        onRegisterRequest = {},
        onRegisterSuccess = {},
    )
}
