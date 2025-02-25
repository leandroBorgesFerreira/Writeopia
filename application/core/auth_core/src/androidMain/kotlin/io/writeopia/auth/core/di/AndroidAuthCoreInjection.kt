package io.writeopia.auth.core.di

import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.writeopia.auth.core.manager.AuthManager
import io.writeopia.auth.core.manager.FirebaseAuthManager
import io.writeopia.auth.core.repository.AuthRepository
import io.writeopia.auth.core.repository.SharedPrefsAuthRepository

class AndroidAuthCoreInjection private constructor(
    private val sharedPreferences: SharedPreferences,
) : AuthCoreInjection {

    private val auth: FirebaseAuth = Firebase.auth

    override fun provideAccountManager(): AuthManager = FirebaseAuthManager(auth)

    override fun provideAuthRepository(): AuthRepository =
        SharedPrefsAuthRepository(sharedPreferences)

    companion object {
        private var instance: AndroidAuthCoreInjection? = null

        fun singleton(sharedPreferences: SharedPreferences): AndroidAuthCoreInjection =
            instance ?: AndroidAuthCoreInjection(sharedPreferences).also {
                instance = it
            }
    }
}
