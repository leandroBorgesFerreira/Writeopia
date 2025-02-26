package io.writeopia.di

import io.writeopia.auth.core.token.AppBearerTokenHandler
import io.writeopia.sdk.network.injector.ConnectionInjector

object ConnectionInjectorFactory {
    private var instance: ConnectionInjector? = null

    fun singleton(): ConnectionInjector =
        instance ?: ConnectionInjector(
            baseUrl = "https://writeopia.io/api",
            bearerTokenHandler = AppBearerTokenHandler,
            apiLogger = ApiLogger,
            disableWebsocket = false,
        ).also {
            instance = it
        }
}
