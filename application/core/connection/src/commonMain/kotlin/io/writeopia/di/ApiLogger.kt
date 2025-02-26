package io.writeopia.di

import io.ktor.client.plugins.logging.Logger

expect object ApiLogger : Logger {
    override fun log(message: String)
}
