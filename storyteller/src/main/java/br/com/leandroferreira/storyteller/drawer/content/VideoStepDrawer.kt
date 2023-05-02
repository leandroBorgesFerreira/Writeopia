package br.com.leandroferreira.storyteller.drawer.content

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import br.com.leandroferreira.storyteller.drawer.StoryUnitDrawer
import br.com.leandroferreira.storyteller.model.StoryStep
import br.com.leandroferreira.storyteller.model.StoryUnit
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.request.videoFrameMillis

/**
 * Draws a simple video thumnail using Coil.
 */
class VideoStepDrawer(private val containerModifier: Modifier? = null) : StoryUnitDrawer {

    @Composable
    override fun Step(step: StoryUnit, editable: Boolean, extraData: Map<String, Any>) {
        val videoStep = step as StoryStep

        Box(modifier = Modifier.padding(vertical = 3.dp, horizontal = 8.dp)) {
            Box(
                modifier = containerModifier ?: Modifier
                    .clip(shape = RoundedCornerShape(size = 12.dp))
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(size = 12.dp)
                    )
                    .align(Alignment.Center)
            ) {
                val request = ImageRequest.Builder(LocalContext.current)
                    .data(videoStep.url?.toUri())
                    .videoFrameMillis(1000)
                    .build()

                AsyncImage(
                    model = request,
                    contentDescription = "",
                    modifier = Modifier
                )
            }

            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .align(alignment = Alignment.TopEnd)
            ) {
                Box(
                    modifier = Modifier
                        .clip(shape = CircleShape)
                        .background(Color(0xFFF2994A))
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Movie,
                        contentDescription = "",
                        tint = Color.White
                    )
                }
            }
        }
    }
}
