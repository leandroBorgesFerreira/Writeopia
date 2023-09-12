package io.storiesteller.navigation

import androidx.navigation.NavController
import io.storiesteller.utils_module.Destinations

internal fun NavController.navigateToNewNote() {
    navigate(Destinations.EDITOR.id)
}

internal fun NavController.navigateToNote(id: String, title: String) {
    navigate("${Destinations.EDITOR.id}/$id/$title")
}

internal fun NavController.navigateToAccount() {
    navigate(Destinations.ACCOUNT.id)
}
