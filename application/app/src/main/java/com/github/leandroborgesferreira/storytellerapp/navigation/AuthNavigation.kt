package com.github.leandroborgesferreira.storytellerapp.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.github.leandroborgesferreira.storytellerapp.auth.di.AuthInjections
import com.github.leandroborgesferreira.storytellerapp.auth.login.LoginScreenBinding
import com.github.leandroborgesferreira.storytellerapp.auth.menu.AuthMenuScreen
import com.github.leandroborgesferreira.storytellerapp.auth.register.RegisterScreen

internal fun NavGraphBuilder.authNavigation(
    navController: NavController,
    authInjections: AuthInjections
) {
    navigation(
        startDestination = Destinations.AUTH_MENU.id,
        route = Destinations.AUTH_MENU_INNER_NAVIGATION.id
    ) {
        composable(Destinations.AUTH_MENU.id) {
            AuthMenuScreen(
                navigateToLogin = navController::navigateAuthLogin,
                navigateToRegister = navController::navigateAuthRegister,
                navigateToApp = navController::navigateToMainMenu
            )
        }

        composable(Destinations.AUTH_REGISTER.id) {
            val registerViewModel = authInjections.provideRegisterViewModel()

            RegisterScreen(
                nameState = registerViewModel.name,
                emailState = registerViewModel.email,
                passwordState = registerViewModel.password,
                registerState = registerViewModel.register,
                nameChanged = registerViewModel::nameChanged,
                emailChanged = registerViewModel::emailChanged,
                passwordChanged = registerViewModel::passwordChanged,
                onRegisterRequest = registerViewModel::onRegister,
                onRegisterSuccess = navController::navigateToMainMenu,
            )
        }

        composable(Destinations.AUTH_LOGIN.id) {
            val loginViewModel = authInjections.provideLoginViewModel()
            LoginScreenBinding(loginViewModel, navController::navigateToMainMenu)
        }
    }
}

internal fun NavController.navigateAuthRegister() {
    navigate(Destinations.AUTH_REGISTER.id)
}

internal fun NavController.navigateAuthLogin() {
    navigate(Destinations.AUTH_LOGIN.id)
}

internal fun NavController.navigateToAuthMenu() {
    navigate(Destinations.AUTH_MENU.id)
}