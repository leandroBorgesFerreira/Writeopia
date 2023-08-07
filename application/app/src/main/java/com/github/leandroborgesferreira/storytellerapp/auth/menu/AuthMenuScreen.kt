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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.leandroborgesferreira.storytellerapp.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun AuthMenuScreen(
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
fun AuthMenuScreenPreview() {
    AuthMenuScreen(
        navigateToLogin = {},
        navigateToRegister = {},
    )
}