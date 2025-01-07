package io.writeopia.navigation

import kotlinx.coroutines.flow.StateFlow

interface NavigationViewModel {
    val selectedNavigation: StateFlow<List<BottomBarItem>>
}

data class BottomBarItem(
    val navItemName: NavItemName,
    val destination: String,
)
