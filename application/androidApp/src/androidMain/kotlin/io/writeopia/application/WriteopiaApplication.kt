package io.writeopia.application

import android.app.Application
import android.util.Log
import io.writeopia.BuildConfig
import io.writeopia.sdk.video.VideoFrameConfig
import io.writeopia.auth.core.AuthInitializer

class WriteopiaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        VideoFrameConfig.configCoilForVideoFrame(this)
        // Todo: Remove BuildConfig.DEBUG check later.
        if (!BuildConfig.DEBUG) {
            try {
                AuthInitializer.initializeAwsAuth(applicationContext)
                Log.i("WriteopiaApplication", "Initialized Amplify")
            } catch (error: Exception) {
                Log.e("WriteopiaApplication", "Could not initialize Amplify", error)
            }
        }
    }
}