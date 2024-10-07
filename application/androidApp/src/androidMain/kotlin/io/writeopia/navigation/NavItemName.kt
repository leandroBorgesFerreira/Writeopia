package io.writeopia.navigation

import io.writeopia.utils_module.Destinations

enum class NavItemName(val value: String) {
    HOME("Home"),
    SEARCH("Search"),
    NOTIFICATIONS("Notifications");

    companion object {
        fun selectRoute(route: String): NavItemName {
            val destination = Destinations.entries.first {
                route.contains(it.id)
            }
            return entries.first { navItem ->
                navItem.value == destination.root
            }
        }
    }
}
