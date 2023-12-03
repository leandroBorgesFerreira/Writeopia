package io.writeopia.account.di

import io.writeopia.account.viewmodel.AccountMenuKmpViewModel
import io.writeopia.auth.core.di.AuthCoreInjection

class AccountMenuKmpInjector(private val authCoreInjection: AuthCoreInjection) {

    internal fun provideAccountMenuKmpViewModel(): AccountMenuKmpViewModel =
        AccountMenuKmpViewModel(
            authManager = authCoreInjection.provideAccountManager(),
            authRepository = authCoreInjection.provideAuthRepository()
        )

}