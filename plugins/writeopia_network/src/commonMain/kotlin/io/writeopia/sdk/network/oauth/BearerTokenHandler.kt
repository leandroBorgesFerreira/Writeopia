package io.writeopia.sdk.network.oauth

interface BearerTokenHandler {
    suspend fun getIdToken(): String?
}
