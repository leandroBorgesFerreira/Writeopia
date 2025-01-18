package io.writeopia.ui.image

import androidx.compose.runtime.Composable
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade

object ImageLoadConfig {

    @Composable
    fun configImageLoad() {
        setSingletonImageLoaderFactory { context ->
            ImageLoader.Builder(context)
                .crossfade(true)
                .build()
        }
    }

}
