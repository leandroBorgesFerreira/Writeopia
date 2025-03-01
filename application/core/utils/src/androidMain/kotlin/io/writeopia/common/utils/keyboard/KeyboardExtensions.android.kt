package io.writeopia.common.utils.keyboard

import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.isCtrlPressed

actual fun KeyEvent.isCommandTrigger(): Boolean = this.isCtrlPressed
