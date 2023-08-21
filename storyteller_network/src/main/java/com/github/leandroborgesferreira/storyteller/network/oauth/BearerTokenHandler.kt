package com.github.leandroborgesferreira.storyteller.network.oauth

interface BearerTokenHandler {
    fun getIdToken(): String

    fun getRefreshToken()
}
