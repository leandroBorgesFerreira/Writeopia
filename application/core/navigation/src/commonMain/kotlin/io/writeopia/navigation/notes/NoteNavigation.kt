package io.writeopia.navigation.notes

import androidx.navigation.NavController
import io.writeopia.utils_module.Destinations

fun NavController.navigateToNewNote() {
    navigate(Destinations.EDITOR.id)
}

fun NavController.navigateToNote(id: String, title: String) {
    navigate("${Destinations.EDITOR.id}/$id/$title")
}

fun NavController.navigateToAccount() {
    navigate(Destinations.ACCOUNT.id)
}
