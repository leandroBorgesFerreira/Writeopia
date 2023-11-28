package io.writeopia.account.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import io.writeopia.account.viewmodel.AccountMenuAndroidViewModel
import io.writeopia.account.viewmodel.AccountMenuViewModel
import io.writeopia.auth.core.di.AuthCoreInjection

class AccountMenuInjector(private val accountMenuKmpInjector: AccountMenuKmpInjector) {

    @Composable
    fun provideAccountMenuAndroidViewModel(): AccountMenuViewModel =
        viewModel { AccountMenuAndroidViewModel(accountMenuKmpInjector.provideAccountMenuKmpViewModel()) }


    companion object {
        fun create(authCoreInjection: AuthCoreInjection) =
            AccountMenuInjector(AccountMenuKmpInjector(authCoreInjection))

    }
}