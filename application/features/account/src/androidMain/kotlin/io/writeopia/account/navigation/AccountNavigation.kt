package io.writeopia.account.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.writeopia.account.ui.AccountMenuScreen
import io.writeopia.account.viewmodel.AccountMenuViewModel
import io.writeopia.utils_module.Destinations

fun NavGraphBuilder.accountMenuNavigation(
    accountMenuViewModel: AccountMenuViewModel,
    navigateToAuthMenu: () -> Unit
) {
    composable(
        Destinations.ACCOUNT.id,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { intSize -> -intSize }
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { intSize -> -intSize }
            )
        }
    ) {
        LaunchedEffect(key1 = "start") {
            accountMenuViewModel.checkLoggedIn()
        }

        AccountMenuScreen(
            accountMenuViewModel = accountMenuViewModel,
            isLoggedInState = accountMenuViewModel.isLoggedIn,
            onLogout = navigateToAuthMenu,
            goToRegister = navigateToAuthMenu
        )
    }
}
