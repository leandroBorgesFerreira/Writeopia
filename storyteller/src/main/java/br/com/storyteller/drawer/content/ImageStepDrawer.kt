package br.com.storyteller.drawer.content

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(size = 12.dp))
                    .background(Color.LightGray)
            ) {
                Box(
                    modifier = Modifier
                        .padding(if (imageStep.title != null) 6.dp else 0.dp)
                        .clip(shape = RoundedCornerShape(size = 12.dp))
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageStep.path?.toUri() ?: imageStep.url)
                            .build(),
                        contentDescription = ""
                    )
                }
                step.title?.let { text ->
                    Text(
                        text = text,
                        modifier = Modifier
                            .padding(6.dp)
                            .fillMaxWidth(),
                        style = TextStyle(color = Color.Black),
                    )
                }
            }
        }
    }
}
