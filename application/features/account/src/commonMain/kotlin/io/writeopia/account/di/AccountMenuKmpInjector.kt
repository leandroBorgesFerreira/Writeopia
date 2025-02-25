package io.writeopia.account.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import io.writeopia.account.viewmodel.AccountMenuKmpViewModel
import io.writeopia.account.viewmodel.AccountMenuViewModel
import io.writeopia.auth.core.di.AuthCoreInjection

class AccountMenuKmpInjector(
    private val authCoreInjection: AuthCoreInjection
) : AccountMenuInjector {

    private fun provideAccountMenuKmpViewModel(): AccountMenuKmpViewModel =
        AccountMenuKmpViewModel(
            authManager = authCoreInjection.provideAccountManager(),
            authRepository = authCoreInjection.provideAuthRepository()
        )

    @Composable
    override fun provideAccountMenuViewModel(): AccountMenuViewModel =
        viewModel { provideAccountMenuKmpViewModel() }

    companion object {

    }
}
