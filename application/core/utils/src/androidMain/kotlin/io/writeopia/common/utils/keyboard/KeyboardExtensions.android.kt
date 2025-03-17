package io.writeopia.common.utils.keyboard

import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.isShiftPressed

actual fun KeyEvent.isCommandTrigger(): Boolean = this.isCtrlPressed

actual fun KeyEvent.isMultiSelectionTrigger(): Boolean = this.isShiftPressed && this.isMetaPressed
