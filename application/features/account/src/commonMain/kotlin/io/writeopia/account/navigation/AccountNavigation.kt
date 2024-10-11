package io.writeopia.account.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.writeopia.account.di.AccountMenuInjector
import io.writeopia.account.ui.AccountMenuScreen
import io.writeopia.common.utils.Destinations
import io.writeopia.model.ColorThemeOption

fun NavGraphBuilder.accountMenuNavigation(
    accountMenuInjector: AccountMenuInjector,
    navigateToAuthMenu: () -> Unit,
    selectColorTheme: (ColorThemeOption) -> Unit,
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
        val accountMenuViewModel = accountMenuInjector.provideAccountMenuViewModel()

        LaunchedEffect(key1 = "start") {
            accountMenuViewModel.checkLoggedIn()
        }

        AccountMenuScreen(
            accountMenuViewModel = accountMenuViewModel,
            isLoggedInState = accountMenuViewModel.isLoggedIn,
            onLogout = navigateToAuthMenu,
            goToRegister = navigateToAuthMenu,
            selectColorTheme = selectColorTheme
        )
    }
}
