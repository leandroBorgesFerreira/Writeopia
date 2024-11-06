package io.writeopia.navigation.presentation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import io.writeopia.common.utils.Destinations
import io.writeopia.note_menu.data.model.NotesNavigation

fun NavController.navigateToPresentation(noteId: String) {
    navigate("${Destinations.PRESENTATION.id}/$noteId")
}
