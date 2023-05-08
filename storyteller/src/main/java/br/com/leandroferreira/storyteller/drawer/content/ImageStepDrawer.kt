package br.com.leandroferreira.storyteller.drawer.content

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.leandroferreira.storyteller.draganddrop.target.DragTarget
import br.com.leandroferreira.storyteller.draganddrop.target.DropTarget
import br.com.leandroferreira.storyteller.drawer.DrawInfo
import br.com.leandroferreira.storyteller.drawer.StoryUnitDrawer
import br.com.leandroferreira.storyteller.model.story.StoryStep
import br.com.leandroferreira.storyteller.model.story.StoryUnit
import br.com.leandroferreira.storyteller.model.change.MergeInfo
import br.com.leandroferreira.storyteller.model.draganddrop.DropInfo
import coil.compose.AsyncImage
import coil.request.ImageRequest

/**
 * Draws a image. Uses Coil to parse the image.
 */
class ImageStepDrawer(
    private val containerModifier: (Boolean) -> Modifier? = { null },
    private val mergeRequest: (MergeInfo) -> Unit = { }
) : StoryUnitDrawer {

    @Composable
    override fun LazyItemScope.Step(step: StoryUnit, drawInfo: DrawInfo) {
        val imageStep = step as StoryStep

        DropTarget(modifier = Modifier.padding(6.dp)) { inBound, data ->
            if (inBound && data != null) {
                mergeRequest(
                    MergeInfo(
                        receiver = imageStep,
                        sender = data.storyUnit,
                        positionFrom = data.positionFrom,
                        positionTo = drawInfo.position
                    )
                )
            }

            val imageModifier = containerModifier(inBound) ?: Modifier.defaultModifier(inBound)
            val focusRequester = remember { FocusRequester() }

            Column(
                modifier = imageModifier
                    .focusRequester(focusRequester)
                    .onGloballyPositioned {
                        if (drawInfo.focusId == step.id) {
                            focusRequester.requestFocus()
                        }
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DragTarget(
                    modifier = imageModifier,
                    dataToDrop = DropInfo(imageStep, drawInfo.position)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageStep.path ?: imageStep.url)
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
        fun Modifier.defaultModifier(inBound: Boolean) =
            clip(shape = RoundedCornerShape(size = 12.dp))
                .background(if (inBound) Color.LightGray else Color.DarkGray)
                .border(width = 1.dp, if (inBound) Color.LightGray else Color.DarkGray)
    }
}
