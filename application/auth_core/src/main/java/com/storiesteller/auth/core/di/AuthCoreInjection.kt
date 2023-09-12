package com.storiesteller.auth.core.di

import android.content.SharedPreferences
import com.storiesteller.auth.core.AuthManager
import com.storiesteller.auth.core.repository.AuthRepository

class AuthCoreInjection(private val sharedPreferences: SharedPreferences) {

    fun provideAccountManager(): AuthManager = AuthManager(sharedPreferences)

    fun provideAuthRepository(
        sharedPreferences: SharedPreferences = this.sharedPreferences
    ): AuthRepository = AuthRepository(sharedPreferences)
}