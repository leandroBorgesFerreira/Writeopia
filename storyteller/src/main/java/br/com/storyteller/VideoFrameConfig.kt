package br.com.storyteller

import android.content.Context
import coil.Coil
import coil.decode.VideoFrameDecoder

object VideoFrameConfig {

    fun configCoilForVideoFrame(context: Context) {
        Coil.imageLoader(context).newBuilder()
            .components {
                add(VideoFrameDecoder.Factory())
            }
            .build()
            .let(Coil::setImageLoader)
    }
}
