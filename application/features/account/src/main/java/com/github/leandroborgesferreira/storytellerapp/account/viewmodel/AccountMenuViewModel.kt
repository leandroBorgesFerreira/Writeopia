package com.github.leandroborgesferreira.storytellerapp.account.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.leandroborgesferreira.storytellerapp.auth.core.AuthManager
import com.github.leandroborgesferreira.storytellerapp.utils_module.ResultData
import com.github.leandroborgesferreira.storytellerapp.utils_module.toBoolean
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AccountMenuViewModel(private val authManager: AuthManager) : ViewModel() {
    private val _isLoggedIn: MutableStateFlow<ResultData<Boolean>> =
        MutableStateFlow(ResultData.Idle())

    val isLoggedIn = _isLoggedIn.asStateFlow()

    fun checkLoggedIn() {
        viewModelScope.launch {
            _isLoggedIn.value = ResultData.Loading()
            _isLoggedIn.value = authManager.isLoggedIn()
        }
    }

    fun logout(onLogOutSuccess: () -> Unit) {
        viewModelScope.launch {
            val result = authManager.logout()

            if (result.toBoolean()) {
                onLogOutSuccess()
            }
        }
    }
}