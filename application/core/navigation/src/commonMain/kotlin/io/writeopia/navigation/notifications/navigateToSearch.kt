package io.writeopia.navigation.notifications

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import io.writeopia.features.notifications.navigation.NotificationsDestiny

fun NavController.navigateToNotifications(builder: NavOptionsBuilder.() -> Unit = {}) {
    this.navigate(NotificationsDestiny.notifications(), builder = builder)
}
