package com.github.leandroborgesferreira.storytellerapp.auth.core.di

import android.content.SharedPreferences
import com.storiesteller.sdkapp.auth.core.AuthManager
import com.github.leandroborgesferreira.storytellerapp.auth.core.repository.AuthRepository

class AuthCoreInjection(private val sharedPreferences: SharedPreferences) {

    fun provideAccountManager(): AuthManager = AuthManager(sharedPreferences)

    fun provideAuthRepository(
        sharedPreferences: SharedPreferences = this.sharedPreferences
    ): AuthRepository = AuthRepository(sharedPreferences)
}