package io.storiesteller.auth.core.di

import android.content.SharedPreferences
import io.storiesteller.auth.core.AuthManager
import io.storiesteller.auth.core.repository.AuthRepository

class AuthCoreInjection(private val sharedPreferences: SharedPreferences) {

    fun provideAccountManager(): AuthManager = AuthManager(sharedPreferences)

    fun provideAuthRepository(
        sharedPreferences: SharedPreferences = this.sharedPreferences
    ): AuthRepository = AuthRepository(sharedPreferences)
}