package io.writeopia.auth.core.token

import io.writeopia.sdk.network.oauth.BearerTokenHandler

expect object AppBearerTokenHandler: BearerTokenHandler {
     override suspend fun getIdToken(): String?
}
