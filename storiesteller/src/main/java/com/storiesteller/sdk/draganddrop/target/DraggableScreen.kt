package com.storiesteller.sdk.draganddrop.target

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize

@Composable
fun DraggableScreen(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val state = remember { DragTargetInfo() }
    CompositionLocalProvider(LocalDragTargetInfo provides state) {
        Box(modifier = modifier.fillMaxSize()) {
            content()

            if (state.isDragging) {
                var targetSize by remember { mutableStateOf(IntSize.Zero) }
                Box(modifier = Modifier
                    .onGloballyPositioned { layoutCoordinates ->
                        targetSize = layoutCoordinates.size
                    }
                    .graphicsLayer {
                        val offset = (state.dragPosition + state.dragOffset)
                        scaleX = 0.8f
                        scaleY = 0.8f
                        alpha = if (targetSize == IntSize.Zero) 0f else .85f
                        translationX = offset.x.minus(targetSize.width / 2)
                        translationY = offset.y - targetSize.height / 2
                    }

                ) {
                    state.draggableComposable?.invoke()
                }
            }
        }
    }
}
