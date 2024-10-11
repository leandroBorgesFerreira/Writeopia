package io.writeopia.features.search.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.writeopia.features.search.DocumentsSearchScreen
import io.writeopia.common.utils.Destinations

object SearchDestiny {
    fun search() = Destinations.SEARCH.id
}

fun NavGraphBuilder.searchNavigation() {
    composable(
        route = SearchDestiny.search(),
    ) { _ ->
        DocumentsSearchScreen()
    }
}
