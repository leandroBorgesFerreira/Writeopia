package com.github.leandroborgesferreira.storytellerapp.account.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.leandroborgesferreira.storytellerapp.auth.core.AccountManager
import com.github.leandroborgesferreira.storytellerapp.utils_module.isSuccess
import kotlinx.coroutines.launch

class AccountMenuViewModel(private val accountManager: AccountManager) : ViewModel() {

    fun logout(onLogOutSuccess: () -> Unit) {
        viewModelScope.launch {
            val result = accountManager.logout()

            if (result.isSuccess()) {
                onLogOutSuccess()
            }

        }
    }
}