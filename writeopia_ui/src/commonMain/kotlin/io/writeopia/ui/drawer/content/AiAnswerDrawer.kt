package io.writeopia.ui.drawer.content

import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.writeopia.sdk.model.action.Action
import io.writeopia.sdk.model.draganddrop.DropInfo
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.ui.components.SwipeBox
import io.writeopia.ui.draganddrop.target.DragRowTarget
import io.writeopia.ui.draganddrop.target.DropTargetHorizontalDivision
import io.writeopia.ui.draganddrop.target.InBounds
import io.writeopia.ui.drawer.StoryStepDrawer
import io.writeopia.ui.model.DrawConfig
import io.writeopia.ui.model.DrawInfo

/**
 * Drawer for a complex message with swipe action, drag and drop logic and a start content to add functionality
 * like a checkbox or a different Composable.
 */
class AiAnswerDrawer(
    private val modifier: Modifier = Modifier,
    private val customBackgroundColor: Color,
    private val onSelected: (Boolean, Int) -> Unit,
    private val dragIconWidth: Dp,
    private val config: DrawConfig,
    private val onDragHover: (Int) -> Unit,
    private val onDragStart: () -> Unit,
    private val onDragStop: () -> Unit,
    private val moveRequest: (Action.Move) -> Unit,
    private val endContent: @Composable ((StoryStep, DrawInfo, Boolean) -> Unit)? = null,
    private val enabled: Boolean,
    private val paddingValues: PaddingValues = PaddingValues(0.dp),
) : StoryStepDrawer {

    @Composable
    override fun Step(step: StoryStep, drawInfo: DrawInfo) {
        val dropInfo = DropInfo(step, drawInfo.position)
        val interactionSource = remember { MutableInteractionSource() }
        val isHovered by interactionSource.collectIsHoveredAsState()
        var showDragIcon by remember { mutableStateOf(false) }

        val (paddingBottom, paddingTop) = step.tags
            .any { it.tag.isTitle() }
            .takeIf { it }
            ?.let { 4 to 16 }
            ?: (0 to 0)

        DropTargetHorizontalDivision(
            modifier = Modifier.padding(bottom = paddingBottom.dp, top = paddingTop.dp)
        ) { inBound, data ->
            when (inBound) {
                InBounds.OUTSIDE -> {}
                InBounds.INSIDE_UP -> {
                    val position = drawInfo.position - 1
                    handleDrag(position, data)
                }

                InBounds.INSIDE_DOWN -> {
                    val position = drawInfo.position
                    handleDrag(position, data)
                }
            }

            SwipeBox(
                modifier = modifier.hoverable(interactionSource),
                defaultColor = customBackgroundColor,
                activeColor = config.selectedColor(),
                activeBorderColor = config.selectedBorderColor(),
                isOnEditState = drawInfo.selectMode,
                swipeListener = { isSelected ->
                    onSelected(isSelected, drawInfo.position)
                },
                paddingValues = paddingValues
            ) {
                DragRowTarget(
                    modifier = Modifier.fillMaxWidth(),
                    dataToDrop = dropInfo,
                    showIcon = showDragIcon || isHovered && enabled,
                    position = drawInfo.position,
                    dragIconWidth = dragIconWidth,
                    onDragStart = onDragStart,
                    onDragStop = onDragStop,
                    isHoldDraggable = drawInfo.selectMode,
                    emptySpaceClick = {},
                    onClick = {
                        onSelected(!drawInfo.selectMode, drawInfo.position)
                    }
                ) {
                    Text(
                        modifier = Modifier.padding(
                            horizontal = config.iaAnswerHorizontalPadding.dp,
                            vertical = config.iaAnswerHorizontalPadding.dp
                        ),
                        text = step.text ?: "", style = TextStyle(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 16.sp,
//                            fontFamily = fontFamily
                        )
                    )

                    endContent?.invoke(step, drawInfo, isHovered)
                }
            }
        }
    }

    private fun handleDrag(position: Int, data: DropInfo?) {
        onDragHover(position)

        if (data != null) {
            moveRequest(
                Action.Move(
                    data.info as StoryStep,
                    positionFrom = data.positionFrom,
                    positionTo = position
                )
            )
        }
    }
}
