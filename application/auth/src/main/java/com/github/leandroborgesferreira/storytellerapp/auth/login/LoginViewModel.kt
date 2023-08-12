package com.github.leandroborgesferreira.storytellerapp.auth.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.kotlin.core.Amplify
import com.github.leandroborgesferreira.storytellerapp.utils_module.ResultData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "LoginViewModel"

/* The NavigationActivity won't leak because it is the single activity of the whole project*/
class LoginViewModel : ViewModel() {

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _loginState: MutableStateFlow<ResultData<Boolean>> =
        MutableStateFlow(ResultData.Idle())
    val loginState = _loginState.asStateFlow()

    fun emailChanged(name: String) {
        _email.value = name
    }

    fun passwordChanged(name: String) {
        _password.value = name
    }

    fun onLoginRequest() {
        _loginState.value = ResultData.Loading()

        viewModelScope.launch {
            try {
                val result = Amplify.Auth.signIn(_email.value, _password.value)
                if (result.isSignedIn) {
                    val session = Amplify.Auth.fetchAuthSession() as AWSCognitoAuthSession
                    Log.i(
                        "AuthQuickstart",
                        "Sign in succeeded. Token: ${session.userPoolTokensResult.value?.idToken}"
                    )
                } else {
                    Log.e("AuthQuickstart", "Sign in not complete")
                }

                _loginState.value = ResultData.Complete(result.isSignedIn)
            } catch (error: AuthException) {
                Log.e("AuthQuickstart", "Sign in failed", error)
                _loginState.value = ResultData.Error(error)
            }
        }
    }
}