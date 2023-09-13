package io.writeopia.sdk.drawer.content

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import io.writeopia.sdk.drawer.DrawInfo
import io.writeopia.sdk.drawer.StoryStepDrawer
import io.writeopia.sdk.models.story.StoryStep
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.request.videoFrameMillis

/**
 * Draws a simple video thumnail using Coil.
 */
class VideoDrawer(private val containerModifier: Modifier? = null) : StoryStepDrawer {

    @Composable
    override fun Step(step: StoryStep, drawInfo: DrawInfo) {

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
                    .data(step.url)
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
