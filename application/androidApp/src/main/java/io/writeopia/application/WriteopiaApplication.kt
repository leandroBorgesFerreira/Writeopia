package io.writeopia.application

import android.app.Application
import android.util.Log
import io.writeopia.sdk.video.VideoFrameConfig
import io.writeopia.auth.core.AuthInitializer

class WriteopiaApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        VideoFrameConfig.configCoilForVideoFrame(this)

        try {
            AuthInitializer.initializeAwsAuth(this)
            Log.i("WriteopiaApplication", "Initialized Amplify")
        } catch (error: Exception) {
            Log.e("WriteopiaApplication", "Could not initialize Amplify", error)
        }
    }
}