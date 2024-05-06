package io.writeopia.navigation.notes

import androidx.navigation.NavController
import io.writeopia.utils_module.Destinations

internal fun NavController.navigateToMainMenu() {
    navigate(Destinations.CHOOSE_NOTE.id)
}

internal fun NavController.navigateToNoteMenu() {
    navigate(Destinations.CHOOSE_NOTE.id)
}
