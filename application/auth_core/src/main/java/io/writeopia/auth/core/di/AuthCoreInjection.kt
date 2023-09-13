package io.writeopia.auth.core.di

import android.content.SharedPreferences
import io.writeopia.auth.core.AuthManager
import io.writeopia.auth.core.repository.AuthRepository

class AuthCoreInjection(private val sharedPreferences: SharedPreferences) {

    fun provideAccountManager(): AuthManager = AuthManager(sharedPreferences)

    fun provideAuthRepository(
        sharedPreferences: SharedPreferences = this.sharedPreferences
    ): AuthRepository = AuthRepository(sharedPreferences)
}