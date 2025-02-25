package io.writeopia.auth.core.di

import io.writeopia.auth.core.manager.AuthManager
import io.writeopia.auth.core.manager.MockAuthManager
import io.writeopia.auth.core.repository.AuthRepository
import io.writeopia.auth.core.repository.MockAuthRepository

actual class AuthCoreInjectionNeo {
    actual fun provideAccountManager(): AuthManager = MockAuthManager()

    actual fun provideAuthRepository(): AuthRepository = MockAuthRepository()

    actual companion object {
        private var instance: AuthCoreInjectionNeo? = null

        actual fun singleton(): AuthCoreInjectionNeo =
            instance ?: AuthCoreInjectionNeo().also {
                instance = it
            }
    }
}
