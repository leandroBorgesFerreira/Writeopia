package io.writeopia.auth.register

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import io.writeopia.common.utils.ResultData
import io.writeopia.common.utils.icons.WrIcons
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.ui.tooling.preview.Preview

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
        when (val register = registerState.collectAsState().value) {
            is ResultData.Complete -> {
                if (register.data) {
                    LaunchedEffect(key1 = "navigation") {
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

            is ResultData.Loading, is ResultData.InProgress -> {
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
    val name by nameState.collectAsState()
    val email by emailState.collectAsState()
    val password by passwordState.collectAsState()
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
                        imageVector = WrIcons.visibilityOff,
                        contentDescription = ""
                    )
                } else {
                    Icon(
                        modifier = Modifier.clickable {
                            showPassword = !showPassword
                        },
                        imageVector = WrIcons.visibilityOn,
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
                text = "Register",
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
