package io.writeopia.ui.draganddrop.target

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import io.writeopia.sdk.model.draganddrop.DropInfo

@Composable
actual fun DragRowTarget(
    modifier: Modifier,
    dataToDrop: DropInfo,
    showIcon: Boolean,
    position: Int,
    emptySpaceClick: () -> Unit,
    dragIconWidth: Dp,
    iconTintOnHover: Color,
    iconTint: Color,
    onDragStart: () -> Unit,
    onDragStop: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    DragRowTargetMobile(
        modifier,
        dataToDrop,
        showIcon,
        position,
        emptySpaceClick,
        dragIconWidth,
        iconTintOnHover,
        iconTint,
        onDragStart,
        onDragStop,
        content
    )
}
