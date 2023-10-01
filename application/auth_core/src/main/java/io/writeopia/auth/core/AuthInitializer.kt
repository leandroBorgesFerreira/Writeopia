package io.writeopia.auth.core

import android.content.Context
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.kotlin.core.Amplify

object AuthInitializer {
    fun initializeAwsAuth(context: Context) {
        Amplify.addPlugin(AWSCognitoAuthPlugin())
        Amplify.configure(context)
    }
}