package io.writeopia.navigation

import androidx.lifecycle.ViewModel
import io.writeopia.common.utils.Destinations
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NavigationViewModel : ViewModel() {

    private val routeStack = MutableStateFlow(mutableSetOf<String>())

    private val _selectedNavigation = MutableStateFlow(bottomBatItems())
    val selectedNavigation = _selectedNavigation.asStateFlow()

    private fun bottomBatItems() =
        listOf(
            BottomBarItem(
                navItemName = NavItemName.HOME,
                destination = "${Destinations.CHOOSE_NOTE.id}/{type}/{path}",
            ),
            BottomBarItem(
                navItemName = NavItemName.SEARCH,
                destination = Destinations.SEARCH.id,
            ),
            BottomBarItem(
                navItemName = NavItemName.NOTIFICATIONS,
                destination = Destinations.NOTIFICATIONS.id,
            ),
        )
}

data class BottomBarItem(
    val navItemName: NavItemName,
    val destination: String,
)
