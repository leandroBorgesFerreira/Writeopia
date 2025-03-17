package io.writeopia.common.utils.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.clickableWithoutGettingFocus(block: () -> Unit): Modifier =
    this.pointerInput(Unit) {
        awaitPointerEventScope {
            while (true) {
                val event = awaitPointerEvent()
                event.changes.forEach { pointerInputChange ->
                    if (pointerInputChange.changedToUp()) {
                        block()
                    }
                }
            }
        }
    }
