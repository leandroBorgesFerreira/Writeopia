package io.writeopia.account.viewmodel

import io.writeopia.utils_module.KmpViewModel
import io.writeopia.utils_module.ResultData
import kotlinx.coroutines.flow.StateFlow

interface AccountMenuViewModel : KmpViewModel {

    val isLoggedIn: StateFlow<ResultData<Boolean>>

    fun checkLoggedIn()

    fun logout(onLogOutSuccess: () -> Unit)

    fun eraseOfflineByChoice(navigateToRegister: () -> Unit)
}