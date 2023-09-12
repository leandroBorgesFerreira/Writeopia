package com.storiesteller.auth.core.token

import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.kotlin.core.Amplify
import com.storiesteller.sdk.network.oauth.BearerTokenHandler

object AmplifyTokenHandler : BearerTokenHandler {

    private suspend fun authSession() = (Amplify.Auth.fetchAuthSession() as AWSCognitoAuthSession)

    override suspend fun getIdToken(): String =
        authSession().userPoolTokensResult.value?.idToken ?: ""

    override suspend fun getRefreshToken(): String =
        authSession().userPoolTokensResult.value?.refreshToken ?: ""
}