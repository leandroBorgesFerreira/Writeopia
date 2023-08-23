package com.github.leandroborgesferreira.storytellerapp.auth.menu

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplifyframework.auth.AuthException
import com.amplifyframework.kotlin.core.Amplify
import com.github.leandroborgesferreira.storytellerapp.utils_module.ResultData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class AuthMenuViewModel : ViewModel() {

    private val _isConnected = MutableStateFlow<ResultData<Boolean>>(ResultData.Idle())
    val isConnected = _isConnected.asStateFlow()

    fun checkLoggedIn() {
        viewModelScope.launch(Dispatchers.IO) {
            _isConnected.value = ResultData.Loading()
            try {
                val session = Amplify.Auth.fetchAuthSession()
                Log.i("AmplifyQuickstart", "Auth session = $session")
                _isConnected.value = ResultData.Complete(session.isSignedIn)
            } catch (error: AuthException) {
                Log.e("AmplifyQuickstart", "Failed to fetch auth session", error)
                _isConnected.value = ResultData.Error(error)
            }
        }
    }
}