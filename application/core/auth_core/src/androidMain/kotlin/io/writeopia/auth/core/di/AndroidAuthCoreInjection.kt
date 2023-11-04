package io.writeopia.auth.core.di

import android.content.SharedPreferences
import io.writeopia.auth.core.BuildConfig
import io.writeopia.auth.core.manager.AmplifyAuthManager
import io.writeopia.auth.core.manager.AuthManager
import io.writeopia.auth.core.manager.MockAuthManager
import io.writeopia.auth.core.repository.AuthRepository
import io.writeopia.auth.core.repository.SharedPrefsAuthRepository

class AndroidAuthCoreInjection(private val sharedPreferences: SharedPreferences) : AuthCoreInjection {

    override fun provideAccountManager(): AuthManager = if (!BuildConfig.DEBUG) {
        AmplifyAuthManager(sharedPreferences)
    } else {
        MockAuthManager()
    }

    override fun provideAuthRepository(): AuthRepository = SharedPrefsAuthRepository(sharedPreferences)
}
