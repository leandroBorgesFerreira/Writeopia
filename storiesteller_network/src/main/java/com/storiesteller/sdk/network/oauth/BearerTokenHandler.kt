package com.storiesteller.sdk.network.oauth

interface BearerTokenHandler {
    suspend fun getIdToken(): String

    suspend fun getRefreshToken(): String
}
