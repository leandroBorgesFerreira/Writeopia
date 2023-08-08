package com.github.leandroborgesferreira.storytellerapp.auth.login

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.github.leandroborgesferreira.storytellerapp.navigation.NavigationActivity
import com.github.leandroborgesferreira.storytellerapp.utils_module.ResultData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

private const val TAG = "LoginViewModel"

/* The NavigationActivity won't leak because it is the single activity of the whole project*/
class LoginViewModel : ViewModel() {

    private lateinit var auth: FirebaseAuth

    fun init() {
        auth = Firebase.auth
    }

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _loginState: MutableStateFlow<ResultData<Unit>> =
        MutableStateFlow(ResultData.Idle())
    val loginState = _loginState.asStateFlow()

    fun emailChanged(name: String) {
        _email.value = name
    }

    fun passwordChanged(name: String) {
        _password.value = name
    }

    fun onLoginRequest(activity: Activity) {
        _loginState.value = ResultData.Loading()

        auth.signInWithEmailAndPassword(email.value, password.value)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    _loginState.value = ResultData.Complete(Unit)
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser

                    Log.d(TAG, "sign in:success. User: ${user?.email}")
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    _loginState.value = ResultData.Idle()
                }
            }
    }
}