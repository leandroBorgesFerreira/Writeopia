package com.github.leandroborgesferreira.storytellerapp.auth.menu

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.leandroborgesferreira.storytellerapp.auth.USER_OFFLINE
import com.github.leandroborgesferreira.storytellerapp.auth.core.AuthManager
import com.github.leandroborgesferreira.storytellerapp.utils_module.ResultData
import com.github.leandroborgesferreira.storytellerapp.utils_module.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class AuthMenuViewModel(
    private val authManager: AuthManager,
    private val sharedPreferences: SharedPreferences,
) : ViewModel() {

    private val _isConnected = MutableStateFlow<ResultData<Boolean>>(ResultData.Idle())
    val isConnected = _isConnected.asStateFlow()

    fun checkLoggedIn() {
        viewModelScope.launch(Dispatchers.IO) {
            _isConnected.value = ResultData.Loading()
            _isConnected.value =
                authManager.isLoggedIn().map { isConnected ->
                    isConnected || sharedPreferences.getBoolean(USER_OFFLINE, false)
                }
        }
    }

    fun saveUserChoiceOffline() {
        sharedPreferences.edit().putBoolean(USER_OFFLINE, true).apply()
    }
}