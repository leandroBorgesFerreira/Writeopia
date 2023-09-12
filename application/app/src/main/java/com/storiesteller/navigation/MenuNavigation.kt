package com.storiesteller.navigation

import androidx.navigation.NavController
import com.storiesteller.utils_module.Destinations


internal fun NavController.navigateToMainMenu() {
    navigate(Destinations.CHOOSE_NOTE.id)
}

internal fun NavController.navigateToNoteMenu() {
    navigate(Destinations.CHOOSE_NOTE.id)
}