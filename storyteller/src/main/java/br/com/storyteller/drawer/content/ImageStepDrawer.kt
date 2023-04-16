package br.com.storyteller.drawer.content

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import br.com.storyteller.drawer.StoryUnitDrawer
import br.com.storyteller.model.StoryStep
import br.com.storyteller.model.StoryUnit
import coil.compose.AsyncImage
import coil.request.ImageRequest

class ImageStepDrawer : StoryUnitDrawer {

    @Composable
    override fun Step(step: StoryUnit) {
        val imageStep = step as StoryStep

        Box(modifier = Modifier.padding(vertical = 3.dp)) {
            Box(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(size = 12.dp))
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(size = 12.dp)
                    )
                    .align(Alignment.Center)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageStep.path?.toUri() ?: imageStep.url)
                        .build(),
                    contentDescription = ""
                )
            }
        }
    }
}