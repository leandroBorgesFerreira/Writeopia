package io.writeopia.auth.core.di

import android.content.SharedPreferences
import io.writeopia.auth.core.BuildConfig
import io.writeopia.auth.core.manager.AmplifyAuthManager
import io.writeopia.auth.core.manager.AuthManager
import io.writeopia.auth.core.manager.MockAuthManager
import io.writeopia.auth.core.repository.AuthRepository

class AuthCoreInjection(private val sharedPreferences: SharedPreferences) {

    fun provideAccountManager(): AuthManager = if (!BuildConfig.DEBUG) {
        AmplifyAuthManager(sharedPreferences)
    } else {
        MockAuthManager()
    }

    fun provideAuthRepository(
        sharedPreferences: SharedPreferences = this.sharedPreferences
    ): AuthRepository = AuthRepository(sharedPreferences)
}