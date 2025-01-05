package io.writeopia.mobile

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import io.writeopia.common.utils.icons.WrIcons
import io.writeopia.navigation.notes.navigateToNoteMenu
import io.writeopia.navigation.notifications.navigateToNotifications
import io.writeopia.navigation.search.navigateToSearch
import io.writeopia.notemenu.data.model.NotesNavigation

private fun NavHostController.navigateToItem(
    navItem: NavItemName,
    builder: NavOptionsBuilder.() -> Unit
) {
    when (navItem) {
        NavItemName.HOME -> this.navigateToNoteMenu(io.writeopia.notemenu.data.model.NotesNavigation.Root, builder)
        NavItemName.SEARCH -> this.navigateToSearch(builder)
        NavItemName.NOTIFICATIONS -> this.navigateToNotifications(builder)
    }
}

private fun NavItemName.iconForNavItem(): ImageVector =
    when (this) {
        NavItemName.HOME -> io.writeopia.common.utils.icons.WrIcons.home
        NavItemName.SEARCH -> io.writeopia.common.utils.icons.WrIcons.search
        NavItemName.NOTIFICATIONS -> io.writeopia.common.utils.icons.WrIcons.notifications
    }
