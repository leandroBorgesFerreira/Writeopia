package io.writeopia.navigation.notifications

import androidx.navigation.NavController
import io.writeopia.features.notifications.navigation.NotificationsDestiny

fun NavController.navigateToNotifications() {
    this.navigate(NotificationsDestiny.notifications())
}
