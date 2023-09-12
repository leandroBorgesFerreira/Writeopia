package com.storiesteller.sdk.video

import android.content.Context
import coil.Coil
import coil.decode.VideoFrameDecoder

/**
 * Configs Coil to show Video Thumbnails. Please call [VideoFrameConfig.configCoilForVideoFrame] if
 * you pretend to show Thumbnails for videos.
 */
object VideoFrameConfig {

    /**
     * Configures Coil to show thumnails for videos.
     */
    fun configCoilForVideoFrame(context: Context) {
        Coil.imageLoader(context).newBuilder()
            .components {
                add(VideoFrameDecoder.Factory())
            }
            .build()
            .let(Coil::setImageLoader)
    }
}
