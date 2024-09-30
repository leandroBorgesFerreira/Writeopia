package io.writeopia.navigation.search

import androidx.navigation.NavController
import io.writeopia.features.search.navigation.SearchDestiny

fun NavController.navigateToSearch() {
    this.navigate(SearchDestiny.search())
}
