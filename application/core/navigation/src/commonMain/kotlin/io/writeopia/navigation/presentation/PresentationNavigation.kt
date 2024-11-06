package io.writeopia.navigation.presentation

import androidx.navigation.NavController
import io.writeopia.common.utils.Destinations

fun NavController.navigateToPresentation(noteId: String) {
    navigate("${Destinations.PRESENTATION.id}/$noteId")
}
