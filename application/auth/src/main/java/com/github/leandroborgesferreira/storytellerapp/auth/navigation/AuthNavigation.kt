package com.github.leandroborgesferreira.storytellerapp.auth.navigation

import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.github.leandroborgesferreira.storytellerapp.auth.di.AuthInjection
import com.github.leandroborgesferreira.storytellerapp.auth.login.LoginScreenBinding
import com.github.leandroborgesferreira.storytellerapp.auth.menu.AuthMenuScreen
import com.github.leandroborgesferreira.storytellerapp.auth.menu.AuthMenuViewModel
import com.github.leandroborgesferreira.storytellerapp.auth.register.RegisterScreen
import com.github.leandroborgesferreira.storytellerapp.utils_module.Destinations

fun NavGraphBuilder.authNavigation(
    navController: NavController,
    authInjection: AuthInjection,
    toAppNavigation: () -> Unit
) {
    navigation(
        startDestination = Destinations.AUTH_MENU.id,
        route = Destinations.AUTH_MENU_INNER_NAVIGATION.id
    ) {
        composable(Destinations.AUTH_MENU.id) {
            Log.d("NavGraphBuilder", "Destinations.AUTH_MENU.id")
            val authMenuViewModel: AuthMenuViewModel =
                viewModel(initializer = { AuthMenuViewModel() })

            LaunchedEffect(key1 = true, block = {
                authMenuViewModel.checkLoggedIn()
            })

            AuthMenuScreen(
                navigateToLogin = navController::navigateAuthLogin,
                navigateToRegister = navController::navigateAuthRegister,
                navigateToApp = toAppNavigation,
                isConnectedState = authMenuViewModel.isConnected
            )
        }

        composable(Destinations.AUTH_REGISTER.id) {
            val registerViewModel = authInjection.provideRegisterViewModel()

            RegisterScreen(
                nameState = registerViewModel.name,
                emailState = registerViewModel.email,
                passwordState = registerViewModel.password,
                registerState = registerViewModel.register,
                nameChanged = registerViewModel::nameChanged,
                emailChanged = registerViewModel::emailChanged,
                passwordChanged = registerViewModel::passwordChanged,
                onRegisterRequest = registerViewModel::onRegister,
                onRegisterSuccess = toAppNavigation,
            )
        }

        composable(Destinations.AUTH_LOGIN.id) {
            val loginViewModel = authInjection.provideLoginViewModel()
            LoginScreenBinding(loginViewModel, toAppNavigation)
        }
    }
}

internal fun NavController.navigateAuthRegister() {
    navigate(Destinations.AUTH_REGISTER.id)
}

internal fun NavController.navigateAuthLogin() {
    navigate(Destinations.AUTH_LOGIN.id)
}

fun NavController.navigateToAuthMenu() {
    navigate(Destinations.AUTH_MENU.id)
}