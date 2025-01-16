package io.writeopia.auth.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.writeopia.common.utils.ResultData
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AuthMenuScreen(
    isConnectedState: StateFlow<ResultData<Boolean>>,
    saveUserChoiceOffline: () -> Unit,
    navigateToLogin: () -> Unit,
    navigateToRegister: () -> Unit,
    navigateToApp: () -> Unit
) {
    when (val isConnected = isConnectedState.collectAsState().value) {
        is ResultData.Complete -> {
            if (isConnected.data) {
                LaunchedEffect("navigateUp") {
                    navigateToApp()
                }
            } else {
                AuthMenuContentScreen(
                    navigateToLogin,
                    navigateToRegister,
                    navigateToApp,
                    saveUserChoiceOffline
                )
            }
        }

        is ResultData.Error -> {
            AuthMenuContentScreen(
                navigateToLogin,
                navigateToRegister,
                navigateToApp,
                saveUserChoiceOffline
            )
        }

        is ResultData.Idle, is ResultData.Loading -> {
            LoadingScreen()
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
private fun AuthMenuContentScreen(
    navigateToLogin: () -> Unit,
    navigateToRegister: () -> Unit,
    saveUserChoiceOffline: () -> Unit,
    navigateToApp: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
//        Image(
//            modifier = Modifier.fillMaxWidth(),
//            painter = painterResource(id = R.drawable.top_background_auth),
//            contentDescription = "",
//            contentScale = ContentScale.FillWidth,
//        )
//
//        Image(
//            modifier = Modifier.align(Alignment.BottomEnd),
//            painter = painterResource(id = R.drawable.bottom_end_corner_auth_background),
//            contentDescription = "",
//        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))

//            Image(
//                modifier = Modifier
//                    .height(270.dp)
//                    .padding(vertical = 25.dp),
//                painter = painterResource(id = R.drawable.ic_auth_menu_logo),
//                contentDescription = "",
//                contentScale = ContentScale.FillHeight,
//            )

            Spacer(modifier = Modifier.weight(1F))

            TextButton(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .fillMaxWidth(),
                onClick = navigateToLogin
            ) {
                Text(
                    text = "Sign in",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            TextButton(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .fillMaxWidth(),
                onClick = navigateToRegister
            ) {
                Text(
                    text = "Sign up with email",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            TextButton(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .fillMaxWidth(),
                onClick = {
                    saveUserChoiceOffline()
                    navigateToApp()
                }
            ) {
                Text(
                    text = "Enter without register",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

@Preview
@Composable
fun AuthMenuContentScreenPreview() {
    Surface {
        AuthMenuContentScreen(
            navigateToLogin = {},
            navigateToRegister = {},
            navigateToApp = {},
            saveUserChoiceOffline = {}
        )
    }
}
