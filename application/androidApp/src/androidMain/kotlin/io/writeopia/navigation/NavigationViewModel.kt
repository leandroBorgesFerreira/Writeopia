package io.writeopia.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavBackStackEntry
import io.writeopia.utils_module.Destinations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NavigationViewModel : ViewModel() {

    private val routeStack = MutableStateFlow( mutableSetOf<String>())

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


