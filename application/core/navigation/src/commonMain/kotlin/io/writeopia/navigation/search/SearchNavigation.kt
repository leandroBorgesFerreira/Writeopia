package io.writeopia.navigation.search

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import io.writeopia.features.search.navigation.SearchDestiny

fun NavController.navigateToSearch(builder: NavOptionsBuilder.() -> Unit = {}) {
    this.navigate(SearchDestiny.search(), builder = builder)
}
