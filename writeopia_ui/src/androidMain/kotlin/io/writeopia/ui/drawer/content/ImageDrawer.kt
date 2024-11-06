package io.writeopia.ui.drawer.content

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import io.writeopia.sdk.model.action.Action
import io.writeopia.sdk.model.draganddrop.DropInfo
import io.writeopia.ui.model.DrawInfo
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.ui.draganddrop.target.DragTarget
import io.writeopia.ui.draganddrop.target.DropTarget
import io.writeopia.ui.drawer.StoryStepDrawer

/**
 * Draws a image. Uses Coil to parse the image.
 */
class ImageDrawer(
    private val containerModifier: (Boolean) -> Modifier? = { null },
    private val mergeRequest: (Action.Merge) -> Unit = { }
) : StoryStepDrawer {

    @Composable
    override fun Step(step: StoryStep, drawInfo: DrawInfo) {
        DropTarget(modifier = Modifier.padding(horizontal = 6.dp)) { inBound, data ->
            if (inBound && data != null) {
                mergeRequest(
                    Action.Merge(
                        receiver = step,
                        sender = data.info as StoryStep,
                        positionFrom = data.positionFrom,
                        positionTo = drawInfo.position
                    )
                )
            }

            val imageModifier = containerModifier(inBound) ?: Modifier.defaultImageShape(inBound)
            val focusRequester = remember { FocusRequester() }

            Column(
                modifier = imageModifier
                    .focusRequester(focusRequester)
                    .onGloballyPositioned {
                        if (drawInfo.hasFocus()) {
                            focusRequester.requestFocus()
                        }
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DragTarget(
                    modifier = imageModifier,
                    dataToDrop = DropInfo(step, drawInfo.position)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(step.path ?: step.url)
                            .build(),
                        contentScale = ContentScale.Crop,
                        contentDescription = "",
                        modifier = Modifier.clip(shape = RoundedCornerShape(size = 12.dp))
                    )
                }
                step.text?.let { text ->
                    Text(
                        text = text,
                        modifier = Modifier
                            .padding(6.dp)
                            .fillMaxWidth(),
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        ),
                    )
                }
            }
        }
    }
}

fun Modifier.defaultImageShape(inBound: Boolean) =
    clip(shape = RoundedCornerShape(size = 12.dp))
        .background(if (inBound) Color.LightGray else Color.DarkGray)
        .border(width = 1.dp, if (inBound) Color.LightGray else Color.DarkGray)
