package com.github.leandroborgesferreira.storytellerapp.auth.menu

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.leandroborgesferreira.storytellerapp.R
import com.github.leandroborgesferreira.storytellerapp.utils_module.ResultData

@Composable
fun AuthMenuScreen(
    navigateToLogin: () -> Unit,
    navigateToRegister: () -> Unit,
    navigateToApp: () -> Unit,
    authMenuViewModel: AuthMenuViewModel = viewModel(initializer = { AuthMenuViewModel() })
) {
    authMenuViewModel.checkLoggedIn()

    when (val isConnected = authMenuViewModel.isConnected.collectAsStateWithLifecycle().value) {
        is ResultData.Complete -> {
            if (isConnected.data) {
                SideEffect(navigateToApp)
            } else {
                AuthMenuContentScreen(navigateToLogin, navigateToRegister)
            }
        }
        is ResultData.Error -> {
            AuthMenuContentScreen(navigateToLogin, navigateToRegister)
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
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            modifier = Modifier.height(150.dp),
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "",
            contentScale = ContentScale.FillHeight,
        )

        Image(
            modifier = Modifier.height(15.dp),
            painter = painterResource(id = R.drawable.image_storyteller_logo),
            contentDescription = "",
            contentScale = ContentScale.FillHeight,
        )

        Spacer(modifier = Modifier.weight(1F))

        TextButton(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .fillMaxWidth(),
            onClick = navigateToLogin
        ) {
            Text(text = "Sign in", color = MaterialTheme.colorScheme.onPrimary)
        }

        Spacer(modifier = Modifier.height(10.dp))

        TextButton(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .fillMaxWidth(),
            onClick = navigateToRegister
        ) {
            Text(text = "Sign up with email", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

@Preview
@Composable
fun AuthMenuContentScreenPreview() {
    AuthMenuContentScreen(
        navigateToLogin = {},
        navigateToRegister = {},
    )
}