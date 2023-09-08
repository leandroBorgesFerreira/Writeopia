package com.github.leandroborgesferreira.storytellerapp.auth.core

import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.kotlin.core.Amplify

object AuthInitializer {
    fun initializeAwsAuth() {
        Amplify.addPlugin(AWSCognitoAuthPlugin())
    }
}