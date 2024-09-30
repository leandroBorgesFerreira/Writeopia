package io.writeopia.features.notifications.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.writeopia.features.notifications.NotificationsScreen
import io.writeopia.utils_module.Destinations

object NotificationsDestiny {
    fun notifications() = "${Destinations.NOTIFICATIONS}"
}

fun NavGraphBuilder.notificationsNavigation() {
    composable(
        route = NotificationsDestiny.notifications(),
    ) { _ ->
        NotificationsScreen()
    }
}
