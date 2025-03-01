package io.writeopia.common.utils.keyboard

import androidx.compose.ui.input.key.KeyEvent

expect fun KeyEvent.isCommandTrigger(): Boolean
