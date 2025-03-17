package io.writeopia.common.utils.keyboard

import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.isAltPressed
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.isShiftPressed
import org.jetbrains.skiko.OS
import org.jetbrains.skiko.hostOs

actual fun KeyEvent.isCommandTrigger(): Boolean =
    when (hostOs) {
        OS.Android -> this.isCtrlPressed
        OS.Linux -> this.isCtrlPressed
        OS.Windows -> this.isCtrlPressed
        OS.MacOS -> this.isMetaPressed
        OS.Ios -> this.isMetaPressed
        else -> this.isCtrlPressed
    }

actual fun KeyEvent.isMultiSelectionTrigger(): Boolean = this.isShiftPressed &&
    when (hostOs) {
        OS.Android -> this.isAltPressed
        OS.Linux -> this.isAltPressed
        OS.Windows -> this.isAltPressed
        OS.MacOS -> this.isMetaPressed
        OS.Ios -> this.isMetaPressed
        else -> this.isAltPressed
    }


