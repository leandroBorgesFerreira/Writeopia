package com.github.leandroborgesferreira.storytellerapp.navigation

import androidx.navigation.NavController


internal fun NavController.navigateAuthRegister() {
    navigate(Destinations.AUTH_REGISTER.id)
}

internal fun NavController.navigateAuthLogin() {
    navigate(Destinations.AUTH_LOGIN.id)
}

internal fun NavController.navigateToAuthMenu() {
    navigate(Destinations.AUTH_MENU.id)
}