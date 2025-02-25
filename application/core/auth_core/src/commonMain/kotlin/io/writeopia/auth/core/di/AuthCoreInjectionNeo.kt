package io.writeopia.auth.core.di

import io.writeopia.auth.core.manager.AuthManager
import io.writeopia.auth.core.repository.AuthRepository

expect class AuthCoreInjectionNeo {

    fun provideAccountManager(): AuthManager

    fun provideAuthRepository(): AuthRepository

    companion object {
        fun singleton(): AuthCoreInjectionNeo
    }
}
