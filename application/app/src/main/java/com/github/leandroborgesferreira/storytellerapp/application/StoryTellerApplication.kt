package com.github.leandroborgesferreira.storytellerapp.application

import android.app.Application
import android.util.Log
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.kotlin.core.Amplify

class StoryTellerApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.configure(applicationContext)
            Log.i("StoryTellerApplication", "Initialized Amplify")
        } catch (error: AmplifyException) {
            Log.e("StoryTellerApplication", "Could not initialize Amplify", error)
        }
    }
}