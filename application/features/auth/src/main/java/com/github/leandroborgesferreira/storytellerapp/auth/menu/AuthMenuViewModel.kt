package com.storiesteller.sdkapp.auth.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.storiesteller.sdkapp.auth.core.AuthManager
import com.github.leandroborgesferreira.storytellerapp.auth.core.repository.AuthRepository
import com.storiesteller.sdkapp.utils_module.ResultData
import com.storiesteller.sdkapp.utils_module.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class AuthMenuViewModel(
    private val authManager: AuthManager,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isConnected = MutableStateFlow<ResultData<Boolean>>(ResultData.Idle())
    val isConnected = _isConnected.asStateFlow()

    fun checkLoggedIn() {
        viewModelScope.launch(Dispatchers.IO) {
            _isConnected.value = ResultData.Loading()
            _isConnected.value =
                authManager.isLoggedIn().map { isConnected ->
                    isConnected || authRepository.isUserOfflineByChoice()
                }
        }
    }

    fun saveUserChoiceOffline() {
        authRepository.saveUserChoiceOffline()
    }
}