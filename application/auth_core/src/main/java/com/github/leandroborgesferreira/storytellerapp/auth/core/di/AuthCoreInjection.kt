package com.github.leandroborgesferreira.storytellerapp.auth.core.di

import android.content.SharedPreferences
import com.github.leandroborgesferreira.storytellerapp.auth.core.AuthManager

class AuthCoreInjection(private val sharedPreferences: SharedPreferences) {

    fun provideAccountManager(): AuthManager = AuthManager(sharedPreferences)
}