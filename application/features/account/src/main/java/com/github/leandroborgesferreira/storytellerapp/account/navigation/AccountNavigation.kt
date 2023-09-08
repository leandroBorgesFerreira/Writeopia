package com.github.leandroborgesferreira.storytellerapp.account.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.leandroborgesferreira.storytellerapp.account.ui.AccountMenuScreen
import com.github.leandroborgesferreira.storytellerapp.account.viewmodel.AccountMenuViewModel
import com.github.leandroborgesferreira.storytellerapp.auth.core.AccountManager
import com.github.leandroborgesferreira.storytellerapp.utils_module.Destinations

fun NavGraphBuilder.accountMenuNavigation(accountManager: AccountManager, navigateToAuthMenu: () -> Unit) {
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
        AccountMenuScreen(
            accountMenuViewModel = AccountMenuViewModel(accountManager),
            onLogoutSuccess = navigateToAuthMenu
        )
    }
}
