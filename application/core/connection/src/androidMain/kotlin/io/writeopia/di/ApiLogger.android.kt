package io.writeopia.di

import android.util.Log
import io.ktor.client.plugins.logging.Logger

actual object ApiLogger : Logger {
    actual override fun log(message: String) {
        Log.d("AndroidLogger", message)
    }
}
