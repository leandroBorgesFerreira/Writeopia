package io.writeopia.account.di

import androidx.compose.runtime.Composable
import io.writeopia.account.viewmodel.AccountMenuViewModel

interface AccountMenuInjector {

    @Composable
    fun provideAccountMenuViewModel(): AccountMenuViewModel

}
