package io.writeopia.api.editor_spring.auth

import io.writeopia.sdk.network.oauth.BearerTokenHandler

internal class FixedTokenHandler(private val token: String) : BearerTokenHandler {

    override suspend fun getIdToken(): String = token

}