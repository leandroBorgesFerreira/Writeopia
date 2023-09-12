package com.storiesteller.sdk.drawer.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.storiesteller.sdk.draganddrop.target.DropTarget
import com.storiesteller.sdk.drawer.DrawInfo
import com.storiesteller.sdk.drawer.StoryStepDrawer
import com.storiesteller.sdk.model.action.Action
import com.storiesteller.sdk.models.story.StoryStep

/**
 * Draws a white space. This Drawer is very important for accepting drop os other Composables for
 * reorder purposes. A space create a move request when dropping Composables in it while the other
 * story units create a mergeRequest.
 */
class SpaceDrawer(private val moveRequest: (Action.Move) -> Unit = {}) : StoryStepDrawer {

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

            val spaceBgColor =
                when {
                    inBound -> Color.LightGray
//                    BuildConfig.DEBUG -> Color.Cyan
                    else -> Color.Transparent
                }

            Box(
                modifier = Modifier
                    .height(10.dp)
                    .fillMaxWidth()
                    .background(spaceBgColor)
            )
        }
    }
}
