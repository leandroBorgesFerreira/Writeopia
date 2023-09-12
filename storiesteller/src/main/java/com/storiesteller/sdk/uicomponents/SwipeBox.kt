package com.storiesteller.sdk.uicomponents

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.IntOffset
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.roundToInt

@Composable
fun SwipeBox(
    modifier: Modifier = Modifier,
    state: Boolean,
    defaultColor: Color = MaterialTheme.colorScheme.background,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    cornersShape: Shape = MaterialTheme.shapes.medium,
    swipeListener: (Boolean) -> Unit,
    content: @Composable () -> Unit
) {
    var isOnEditMode by remember { mutableStateOf(state) }
    val haptic = LocalHapticFeedback.current
    var swipeOffset by remember { mutableFloatStateOf(0F) }
    var dragging by remember { mutableStateOf(false) }

    val transition = updateTransition(
        targetState = isOnEditMode,
        label = "editionMultipleAnimation",
    )

    val colorAnimated by transition.animateColor(label = "colorAnimation") { isEdit ->
        if (isEdit) activeColor else defaultColor
    }

    val animatedOffset by animateIntOffsetAsState(
        targetValue = IntOffset(swipeOffset.roundToInt(), 0),
        animationSpec =
        spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            visibilityThreshold = IntOffset(1, 1)
        ),
        label = "offsetAnimation",
        finishedListener = {
            swipeListener(isOnEditMode)
        }
    )

    Box(modifier = modifier
        .offset {
            if (dragging) {
                IntOffset(swipeOffset.roundToInt(), 0)
            } else {
                animatedOffset
            }
        }
        .clip(cornersShape)
        .background(colorAnimated)
        .pointerInput(Unit) {
            detectHorizontalDragGestures(
                onDragStart = { _ -> dragging = true },
                onHorizontalDrag = { _, dragAmount ->
                    val maxDistance = 80
                    val correction = (maxDistance - swipeOffset.absoluteValue) / maxDistance

                    swipeOffset += dragAmount * correction.pow(3)
                },
                onDragCancel = {
                    swipeOffset = 0F
                    dragging = false
                },
                onDragEnd = {
                    dragging = false

                    if (swipeOffset.absoluteValue > 40) {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        isOnEditMode = !isOnEditMode
                    }

                    swipeOffset = 0F
                }
            )
        }
    ) {
        content()
    }
}