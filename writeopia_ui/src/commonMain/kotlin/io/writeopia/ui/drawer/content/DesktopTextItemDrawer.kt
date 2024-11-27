package io.writeopia.ui.drawer.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.writeopia.sdk.model.action.Action
import io.writeopia.sdk.model.draganddrop.DropInfo
import io.writeopia.ui.model.DrawInfo
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.Tags
import io.writeopia.ui.components.SwipeBox
import io.writeopia.ui.draganddrop.target.DragRowTarget
import io.writeopia.ui.draganddrop.target.DropTargetHorizontalDivision
import io.writeopia.ui.draganddrop.target.InBounds
import io.writeopia.ui.drawer.SimpleTextDrawer
import io.writeopia.ui.drawer.StoryStepDrawer

/**
 * Drawer for a complex message with swipe action, drag and drop logic and a start content to add functionality
 * like a checkbox or a different Composable.
 */
class DesktopTextItemDrawer(
    private val modifier: Modifier,
    private val customBackgroundColor: Color,
    private val clickable: Boolean,
    private val onSelected: (Boolean, Int) -> Unit,
    private val dragIconWidth: Dp,
    private val onDragHover: (Int) -> Unit,
    private val onDragStart: () -> Unit,
    private val onDragStop: () -> Unit,
    private val moveRequest: (Action.Move) -> Unit,
    private val startContent: @Composable ((StoryStep, DrawInfo) -> Unit)?,
    private val messageDrawer: @Composable RowScope.() -> SimpleTextDrawer
) : StoryStepDrawer {

    @Composable
    override fun Step(step: StoryStep, drawInfo: DrawInfo) {
        val focusRequester = remember { FocusRequester() }
        val dropInfo = DropInfo(step, drawInfo.position)
        val interactionSource = remember { MutableInteractionSource() }
        val isHovered by interactionSource.collectIsHoveredAsState()

        val (paddingBottom, paddingTop) = step.tags
            .asSequence()
            .map(Tags::fromString)
            .any { it?.isTitle() == true }
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
                modifier = modifier
                    .hoverable(interactionSource)
                    .apply {
                        if (clickable) {
                            clickable {
                                focusRequester.requestFocus()
                            }
                        }
                    },
                defaultColor = customBackgroundColor,
                activeColor = MaterialTheme.colorScheme.primary,
                isOnEditState = drawInfo.selectMode,
                swipeListener = { isSelected ->
                    onSelected(isSelected, drawInfo.position)
                }
            ) {
                DragRowTarget(
                    modifier = Modifier
                        .fillMaxWidth()
                        .apply {
                            if (clickable) {
                                clickable {
                                    focusRequester.requestFocus()
                                }
                            }
                        },
                    dataToDrop = dropInfo,
                    showIcon = isHovered,
                    position = drawInfo.position,
                    dragIconWidth = dragIconWidth,
                    onDragStart = onDragStart,
                    onDragStop = onDragStop,
                    emptySpaceClick = {
                        focusRequester.requestFocus()
                    }
                ) {
                    val interactionSourceText = remember { MutableInteractionSource() }

                    startContent?.invoke(step, drawInfo)

                    messageDrawer().Text(
                        step = step,
                        drawInfo = drawInfo,
                        interactionSource = interactionSourceText,
                        focusRequester = focusRequester,
                        decorationBox = @Composable { innerTextField -> innerTextField() },
                    )
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
