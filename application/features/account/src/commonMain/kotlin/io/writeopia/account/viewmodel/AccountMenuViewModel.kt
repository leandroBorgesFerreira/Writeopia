package io.writeopia.account.viewmodel

import io.writeopia.common.utils.ResultData
import kotlinx.coroutines.flow.StateFlow

interface AccountMenuViewModel {

    val isLoggedIn: StateFlow<ResultData<Boolean>>

    fun checkLoggedIn()

    fun logout(onLogOutSuccess: () -> Unit)

    fun eraseOfflineByChoice(navigateToRegister: () -> Unit)
}
