package io.writeopia.common.utils.keyboard

import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.isShiftPressed

expect fun KeyEvent.isCommandTrigger(): Boolean

expect fun KeyEvent.isMultiSelectionTrigger(): Boolean

