package io.writeopia.ui.draganddrop.target

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.writeopia.sdk.model.draganddrop.DropInfo
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes

// Todo: Review this name
@Composable
fun DragCardTargetWithDragItem(
    modifier: Modifier = Modifier,
    dataToDrop: DropInfo,
    showIcon: Boolean = true,
    position: Int,
    dragIconWidth: Dp = 16.dp,
    limitSize: SizeDp? = null,
    content: @Composable BoxScope.() -> Unit
) {
    var currentPosition by remember { mutableStateOf(Offset.Zero) }
    val currentState = LocalDragTargetInfo.current
    val haptic = LocalHapticFeedback.current

    Box(
        modifier = modifier
            .onGloballyPositioned { layoutCoordinates ->
                // Todo: Offset.Zero Is wrong!
                currentPosition = layoutCoordinates.localToWindow(Offset.Zero)
            },
    ) {
        content()

        val showDragIcon = showIcon ||
            currentState.isDragging
            && position == currentState.dataToDrop?.positionFrom

        Crossfade(
            targetState = showDragIcon,
            label = "iconCrossFade",
            animationSpec = tween(durationMillis = 200)
        ) { show ->
            if (show) {
                Icon(
                    modifier = Modifier
                        .padding(6.dp)
                        .size(20.dp)
                        .width(dragIconWidth)
                        .pointerInput(Unit) {
                            detectDragGestures(onDragStart = { offset ->
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                                currentState.dataToDrop = dataToDrop
                                currentState.isDragging = true
                                currentState.dragPosition = currentPosition + offset
                                currentState.draggableComposable = {
                                    if (limitSize != null) {
                                        Box(
                                            modifier = Modifier.size(
                                                limitSize.width,
                                                limitSize.height
                                            )
                                        ) {
                                            content()
                                        }
                                    } else {
                                        content()
                                    }
                                }
                            }, onDrag = { change, dragAmount ->
                                change.consume()
                                currentState.dragOffset += Offset(dragAmount.x, dragAmount.y)
                            }, onDragEnd = {
                                currentState.isDragging = false
                                currentState.dragOffset = Offset.Zero
                            }, onDragCancel = {
                                currentState.dragOffset = Offset.Zero
                                currentState.isDragging = false
                            })
                        }
                        .align(Alignment.TopEnd),
                    imageVector = Icons.Default.DragIndicator,
                    contentDescription = "Drag icon",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}
