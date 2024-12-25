package io.writeopia.navigation.notes

import androidx.navigation.NavController

actual fun NavController.navigateToNote(
    id: String,
    title: String
) {
    this.navigateToNoteMobile(id, title)
}
