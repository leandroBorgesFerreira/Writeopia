package io.writeopia.navigation

import androidx.navigation.NavController

internal fun NavController.navigateToNewNote() {
    navigate(Destinations.EDITOR.id)
}

internal fun NavController.navigateToNote(id: String, title: String) {
    navigate("${Destinations.EDITOR.id}/$id/$title")
}

internal fun NavController.navigateToAccount() {
    navigate(Destinations.ACCOUNT.id)
}
