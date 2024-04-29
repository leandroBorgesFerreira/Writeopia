package io.writeopia.ui.draganddrop.target

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.writeopia.sdk.model.draganddrop.DropInfo

//Todo: Review this name
@Composable
fun DragTargetWithDragItem(
    modifier: Modifier = Modifier,
    dataToDrop: DropInfo,
    showIcon: Boolean = true,
    position: Int,
    emptySpaceClick: () -> Unit,
    dragIconWidth: Dp = 16.dp,
    content: @Composable RowScope.() -> Unit
) {
    var currentPosition by remember { mutableStateOf(Offset.Zero) }
    val currentState = LocalDragTargetInfo.current
    val haptic = LocalHapticFeedback.current

    Row(
        modifier = modifier
            .onGloballyPositioned { layoutCoordinates ->
                // Todo: Offset.Zero Is wrong!
                currentPosition = layoutCoordinates.localToWindow(Offset.Zero)
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val showDragIcon = showIcon ||
                currentState.isDragging && position == currentState.dataToDrop?.positionFrom

        Crossfade(
            targetState = showDragIcon,
            label = "iconCrossFade",
            animationSpec = tween(durationMillis = 200)
        ) { show ->
            if (show) {
                Icon(
                    modifier = Modifier
                        .width(dragIconWidth)
                        .pointerInput(Unit) {
                            detectDragGestures(onDragStart = { offset ->
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                                currentState.dataToDrop = dataToDrop
                                currentState.isDragging = true
                                currentState.dragPosition = currentPosition + offset
                                currentState.draggableComposable = { content() }
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
                        },
                    imageVector = Icons.Default.DragIndicator,
                    contentDescription = "Drag icon",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            } else {
                Spacer(modifier = Modifier.width(dragIconWidth).clickable(onClick = emptySpaceClick))
            }
        }

        content()
    }
}
