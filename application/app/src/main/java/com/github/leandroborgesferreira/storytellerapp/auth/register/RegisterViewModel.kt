package com.github.leandroborgesferreira.storytellerapp.auth.register

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

private const val TAG = "RegisterViewModel"

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

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
        auth.createUserWithEmailAndPassword(email.value, password.value)
            .addOnCompleteListener(getApplication()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    Log.d(TAG, "createUserWithEmail:success. User: ${user?.email}")
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                }
            }
    }
}