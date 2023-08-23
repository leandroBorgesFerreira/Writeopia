package com.github.leandroborgesferreira.storytellerapp.navigation

import androidx.navigation.NavController
import com.github.leandroborgesferreira.storytellerapp.utils_module.Destinations


internal fun NavController.navigateToMainMenu() {
    navigate(Destinations.CHOOSE_NOTE.id)
}

internal fun NavController.navigateToNoteMenu() {
    navigate(Destinations.CHOOSE_NOTE.id)
}