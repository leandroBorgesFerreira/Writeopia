package br.com.leandroferreira.storyteller

import android.content.Context
import coil.Coil
import coil.decode.VideoFrameDecoder

/**
 * Configs Coil to show Video Thumnails. Please call [VideoFrameConfig.configCoilForVideoFrame] if
 * you pretend to show Thumnails for videos.
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
