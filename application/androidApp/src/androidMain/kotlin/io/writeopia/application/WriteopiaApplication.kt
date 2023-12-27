package io.writeopia.application

import android.app.Application
import io.writeopia.ui.drawer.video.VideoFrameConfig

class WriteopiaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        VideoFrameConfig.configCoilForVideoFrame(this)
    }
}