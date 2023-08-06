package com.github.leandroborgesferreira.storytellerapp.navigation

import androidx.navigation.NavController

internal fun NavController.navigateToNewNote() {
    navigate(Destinations.NOTE_DETAILS.id)
}

internal fun NavController.navigateToNote(id: String, title: String) {
    navigate("${Destinations.NOTE_DETAILS.id}/$id/$title")
}
