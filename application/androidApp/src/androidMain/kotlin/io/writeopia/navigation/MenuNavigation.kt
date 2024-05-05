package io.writeopia.navigation

import androidx.navigation.NavController

internal fun NavController.navigateToMainMenu() {
    navigate(Destinations.CHOOSE_NOTE.id)
}

internal fun NavController.navigateToNoteMenu() {
    navigate(Destinations.CHOOSE_NOTE.id)
}
