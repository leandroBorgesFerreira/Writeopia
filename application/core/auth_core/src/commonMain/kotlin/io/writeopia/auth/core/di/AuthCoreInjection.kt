package io.writeopia.auth.core.di

import io.writeopia.auth.core.manager.AuthManager
import io.writeopia.auth.core.repository.AuthRepository

interface AuthCoreInjection {

    fun provideAccountManager(): AuthManager

    fun provideAuthRepository(): AuthRepository
}