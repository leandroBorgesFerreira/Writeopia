package io.writeopia.ui.drawer.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.writeopia.sdk.model.action.Action
import io.writeopia.sdk.model.draw.DrawInfo
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.ui.draganddrop.target.DropTarget
import io.writeopia.ui.drawer.StoryStepDrawer

/**
 * Draws an large empty space. This can be used to add a spacial action to the end of an document
 * and hide this behaviour from the user. Indeed use is to move the focus to the available text
 * drawer when clicked.
 */
class LastEmptySpace(
    private val modifier: Modifier = Modifier,
    private val height: Dp = 500.dp,
    private val moveRequest: (Action.Move) -> Unit = {},
    private val click: () -> Unit = {}
) : StoryStepDrawer {

    @Composable
    override fun Step(step: StoryStep, drawInfo: DrawInfo) {
        DropTarget { inBound, data ->
            if (inBound && data != null) {
                moveRequest(
                    Action.Move(
                        data.storyUnit,
                        positionFrom = data.positionFrom,
                        positionTo = drawInfo.position
                    )
                )
            }

            Box(
                modifier = modifier
                    .height(height)
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = click
                    )
                    .testTag("LastEmptySpace")
            )
        }
    }
}
