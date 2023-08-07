package com.github.leandroborgesferreira.storytellerapp.auth.register

import android.util.Log
import androidx.lifecycle.ViewModel
import com.github.leandroborgesferreira.storytellerapp.navigation.NavigationActivity
import com.github.leandroborgesferreira.storytellerapp.utils_module.ResultData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

private const val TAG = "RegisterViewModel"

/* The NavigationActivity won't leak because it is the single activity of the whole project */
class RegisterViewModel(private val activity: NavigationActivity) : ViewModel() {

    private lateinit var auth: FirebaseAuth

    fun init() {
        auth = Firebase.auth
    }

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _register = MutableStateFlow<ResultData<Unit>>(ResultData.Idle())
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

        auth.createUserWithEmailAndPassword(email.value, password.value)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    task.result.user?.updateProfile(
                        userProfileChangeRequest {
                            displayName = name.value
                        }
                    )?.addOnCompleteListener {
                        _register.value = ResultData.Complete(Unit)
                    }
                } else {
                    _register.value = ResultData.Idle()
                }
            }
    }
}