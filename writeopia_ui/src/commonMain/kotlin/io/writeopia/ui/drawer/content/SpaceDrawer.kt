package io.writeopia.ui.drawer.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.writeopia.sdk.model.action.Action
import io.writeopia.ui.model.DrawInfo
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.ui.draganddrop.target.DropTarget
import io.writeopia.ui.drawer.StoryStepDrawer

/**
 * Draws a white space. This Drawer is very important for accepting drop os other Composables for
 * reorder purposes. A space create a move request when dropping Composables in it while the other
 * story units create a mergeRequest.
 */
class SpaceDrawer(
    private val moveRequest: (Action.Move) -> Unit = {},
    private val backgroundColor: Color = Color.Transparent
) : StoryStepDrawer {

    @Composable
    override fun Step(step: StoryStep, drawInfo: DrawInfo) {
        DropTarget { inBound, data ->
            if (inBound && data != null) {
                moveRequest(
                    Action.Move(
                        data.info as StoryStep,
                        positionFrom = data.positionFrom,
                        positionTo = drawInfo.position
                    )
                )
            }

            Box(
                modifier = Modifier
                    .height(10.dp)
                    .fillMaxWidth()
                    .padding(top = 3.dp, bottom = 3.dp, start = 12.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(backgroundColor)
            )
        }
    }
}
