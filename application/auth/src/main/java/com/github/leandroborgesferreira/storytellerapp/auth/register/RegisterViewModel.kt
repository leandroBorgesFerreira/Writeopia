package com.github.leandroborgesferreira.storytellerapp.auth.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.kotlin.core.Amplify
import com.github.leandroborgesferreira.storytellerapp.auth.intronotes.IntroNotesUseCase
import com.github.leandroborgesferreira.storytellerapp.utils_module.ResultData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/* The NavigationActivity won't leak because it is the single activity of the whole project */
internal class RegisterViewModel(private val introNotesUseCase: IntroNotesUseCase) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _register = MutableStateFlow<ResultData<Boolean>>(ResultData.Idle())
    val register = _register.asStateFlow()

    fun nameChanged(name: String) {
        _name.value = name
    }

    fun emailChanged(name: String) {
        _email.value = name
    }

    fun passwordChanged(name: String) {
        _password.value = name
    }

    fun onRegister() {
        _register.value = ResultData.Loading()

        viewModelScope.launch {
            val options = AuthSignUpOptions.builder()
                .userAttribute(AuthUserAttributeKey.email(), _email.value)
                .userAttribute(AuthUserAttributeKey.name(), _name.value)
                .build()

            try {
                val result = Amplify.Auth.signUp(_email.value, _password.value, options)

                if (result.isSignUpComplete) {
                    introNotesUseCase.addIntroNotes()
                    Log.i("AuthQuickStart", "Result: $result")
                    _register.value = ResultData.Complete(true)
                } else {
                    _register.value = ResultData.Complete(false)
                }
            } catch (error: Exception) {
                Log.e("AuthQuickStart", "Sign up failed", error)
                _register.value = ResultData.Error(error)
            }
        }
    }
}