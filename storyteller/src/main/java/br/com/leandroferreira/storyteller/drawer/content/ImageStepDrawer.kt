package br.com.leandroferreira.storyteller.drawer.content

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import br.com.leandroferreira.storyteller.draganddrop.DragTarget
import br.com.leandroferreira.storyteller.draganddrop.DropTarget
import br.com.leandroferreira.storyteller.drawer.StoryUnitDrawer
import br.com.leandroferreira.storyteller.model.StoryStep
import br.com.leandroferreira.storyteller.model.StoryUnit
import coil.compose.AsyncImage
import coil.request.ImageRequest

class ImageStepDrawer(
    private val containerModifier: (Boolean) -> Modifier? = { null },
    private val mergeRequest: (receiverId: String, senderId: String) -> Unit = { _, _ -> }
) : StoryUnitDrawer {

    @Composable
    override fun Step(step: StoryUnit, editable: Boolean, extraData: Map<String, Any>) {
        val imageStep = step as StoryStep

        DropTarget(modifier = Modifier.padding(6.dp)) { inBound, data ->
            if (inBound && data != null) {
                mergeRequest(imageStep.id, data.id)
            }

            val imageModifier = containerModifier(inBound) ?: defaultModifier(inBound)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = imageModifier
            ) {
                DragTarget(modifier = imageModifier, dataToDrop = imageStep) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageStep.path?.toUri() ?: imageStep.url)
                            .build(),
                        contentScale = ContentScale.Crop,
                        contentDescription = "",
                        modifier = Modifier.clip(shape = RoundedCornerShape(size = 12.dp))
                    )
                }
                step.title?.let { text ->
                    Text(
                        text = text,
                        modifier = Modifier
                            .padding(6.dp)
                            .fillMaxWidth(),
                        style = TextStyle(
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        ),
                    )
                }
            }
        }
    }

    companion object {
        fun defaultModifier(inBound: Boolean) =
            Modifier
                .clip(shape = RoundedCornerShape(size = 12.dp))
                .background(if (inBound) Color.LightGray else Color.DarkGray)
                .border(width = 1.dp, if (inBound) Color.LightGray else Color.DarkGray)
    }
}
