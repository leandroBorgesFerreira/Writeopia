package io.writeopia.account.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import io.writeopia.account.viewmodel.AccountMenuKmpViewModel
import io.writeopia.account.viewmodel.AccountMenuViewModel
import io.writeopia.auth.core.di.AuthCoreInjection

class AccountMenuKmpInjector(
    private val authCoreInjection: AuthCoreInjection
) : AccountMenuInjector {

    @Composable
    override fun provideAccountMenuViewModel(): AccountMenuViewModel = viewModel {
        AccountMenuKmpViewModel(
            authManager = authCoreInjection.provideAccountManager(),
            authRepository = authCoreInjection.provideAuthRepository()
        )
    }
}
