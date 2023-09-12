package com.storiesteller.sdkapp.account.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.storiesteller.sdkapp.auth.core.AuthManager
import com.github.leandroborgesferreira.storytellerapp.auth.core.repository.AuthRepository
import com.storiesteller.sdkapp.utils_module.ResultData
import com.storiesteller.sdkapp.utils_module.toBoolean
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AccountMenuViewModel(
    private val authManager: AuthManager,
    private val authRepository: AuthRepository
) : ViewModel() {
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

    fun eraseOfflineByChoice(navigateToRegister: () -> Unit) {
        viewModelScope.launch {
            authRepository.eraseUserChoiceOffline()
            navigateToRegister()
        }
    }
}