package io.writeopia.navigation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NavigationViewModel : ViewModel() {

    private val _selectedNavigation = MutableStateFlow(bottomBatItems(NavItemName.HOME))
    val selectedNavigation = _selectedNavigation.asStateFlow()

    fun selectNavigation(navItem: NavItemName) {
        _selectedNavigation.value = bottomBatItems(navItem)
    }

    private fun bottomBatItems(navItem: NavItemName) =
        listOf(
            BottomBarItem(
                navItemName = NavItemName.HOME,
                destination = "",
                selected = navItem == NavItemName.HOME
            ),
            BottomBarItem(
                navItemName = NavItemName.SEARCH,
                destination = "",
                selected = navItem == NavItemName.SEARCH

            ),
            BottomBarItem(
                navItemName = NavItemName.NOTIFICATIONS,
                destination = "",
                selected = navItem == NavItemName.NOTIFICATIONS
            ),
        )
}

data class BottomBarItem(
    val navItemName: NavItemName,
    val destination: String,
    val selected: Boolean = false
)

enum class NavItemName(val value: String) {
    HOME("Home"),
    SEARCH("Search"),
    NOTIFICATIONS("Notifications"),
}
