package io.writeopia.application

import android.app.Application
import android.util.Log
import com.amplifyframework.AmplifyException
import com.amplifyframework.kotlin.core.Amplify
import io.writeopia.sdk.video.VideoFrameConfig
import io.writeopia.auth.core.AuthInitializer

class WriteopiaApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        VideoFrameConfig.configCoilForVideoFrame(this)

        try {
            AuthInitializer.initializeAwsAuth()
            Amplify.configure(applicationContext)
            Log.i("WriteopiaApplication", "Initialized Amplify")
        } catch (error: AmplifyException) {
            Log.e("WriteopiaApplication", "Could not initialize Amplify", error)
        }
    }
}