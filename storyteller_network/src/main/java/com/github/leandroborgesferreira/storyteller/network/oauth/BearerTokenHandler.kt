package com.github.leandroborgesferreira.storyteller.network.oauth

interface BearerTokenHandler {
    suspend fun getIdToken(): String

    suspend fun getRefreshToken(): String
}
