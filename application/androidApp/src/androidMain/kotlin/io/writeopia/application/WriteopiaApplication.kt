package io.writeopia.application

import android.app.Application
import android.util.Log
import io.writeopia.BuildConfig
import io.writeopia.auth.core.AuthInitializer
import io.writeopia.ui.drawer.video.VideoFrameConfig

class WriteopiaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        VideoFrameConfig.configCoilForVideoFrame(this)
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