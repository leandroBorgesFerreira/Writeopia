package io.writeopia.account.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import io.writeopia.account.viewmodel.AccountMenuAndroidViewModel
import io.writeopia.account.viewmodel.AccountMenuViewModel
import io.writeopia.auth.core.di.AuthCoreInjection

class AndroidAccountMenuInjector(
    private val accountMenuKmpInjector: AccountMenuKmpInjector
) : AccountMenuInjector {

    @Composable
    override fun provideAccountMenuViewModel(): AccountMenuViewModel = viewModel {
        AccountMenuAndroidViewModel(accountMenuKmpInjector.provideAccountMenuKmpViewModel())
    }


    companion object {
        fun create(authCoreInjection: AuthCoreInjection) =
            AndroidAccountMenuInjector(AccountMenuKmpInjector(authCoreInjection))

    }
}
