package com.github.leandroborgesferreira.storytellerapp.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.leandroborgesferreira.storytellerapp.auth.core.AccountManager
import com.github.leandroborgesferreira.storytellerapp.auth.intronotes.IntroNotesUseCase
import com.github.leandroborgesferreira.storytellerapp.utils_module.ResultData
import com.github.leandroborgesferreira.storytellerapp.utils_module.isSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/* The NavigationActivity won't leak because it is the single activity of the whole project*/
internal class LoginViewModel(
    private val introNotesUseCase: IntroNotesUseCase,
    private val accountManager: AccountManager
) : ViewModel() {

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
            val result = accountManager.signIn(_email.value, _password.value)

            if (result.isSuccess()) {
                introNotesUseCase.addIntroNotes()
            }

            _loginState.value = result
        }
    }
}