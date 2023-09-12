package com.storiesteller.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.storiesteller.auth.core.AuthManager
import com.storiesteller.auth.intronotes.IntroNotesUseCase
import com.storiesteller.utils_module.ResultData
import com.storiesteller.utils_module.toBoolean
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/* The NavigationActivity won't leak because it is the single activity of the whole project */
internal class RegisterViewModel(
    private val introNotesUseCase: IntroNotesUseCase,
    private val authManager: AuthManager
) : ViewModel() {

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

    fun emailChanged(email: String) {
        _email.value = email
    }

    fun passwordChanged(password: String) {
        _password.value = password
    }

    fun onRegister() {
        _register.value = ResultData.Loading()

        viewModelScope.launch {
            val result = authManager.signUp(_email.value, _password.value, _name.value)
            if (result.toBoolean()) {
                introNotesUseCase.addIntroNotes()
            }

            _register.value = result
        }
    }
}