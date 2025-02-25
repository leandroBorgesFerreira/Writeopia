package io.writeopia.auth.core.di

import io.writeopia.auth.core.manager.AuthManager
import io.writeopia.auth.core.manager.MockAuthManager
import io.writeopia.auth.core.repository.AuthRepository
import io.writeopia.auth.core.repository.MockAuthRepository

class KmpAuthCoreInjection private constructor(): AuthCoreInjection {

    override fun provideAccountManager(): AuthManager = MockAuthManager()

    override fun provideAuthRepository(): AuthRepository = MockAuthRepository()

    companion object {
        private var instance: KmpAuthCoreInjection? = null

        fun singleton(): KmpAuthCoreInjection =
            instance ?: KmpAuthCoreInjection().also {
                instance = it
            }
    }
}
