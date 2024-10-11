package io.writeopia.auth.core.token

import io.writeopia.sdk.network.oauth.BearerTokenHandler

// Todo: Fix auth of desktop app!
object MockTokenHandler : BearerTokenHandler {

    override suspend fun getIdToken(): String = "mock"
}