package io.writeopia.desktop

import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.type

internal fun isUndoKeyboardEvent(keyEvent: KeyEvent) =
    keyEvent.isMetaPressed &&
        keyEvent.awtEventOrNull?.keyCode == java.awt.event.KeyEvent.VK_Z &&
        keyEvent.type == KeyEventType.KeyDown

internal fun isSelectionKeyEventStart(keyEvent: KeyEvent) =
    keyEvent.awtEventOrNull?.keyCode == java.awt.event.KeyEvent.VK_ALT &&
        keyEvent.type == KeyEventType.KeyDown

internal fun isSelectionKeyEventStop(keyEvent: KeyEvent) =
    keyEvent.awtEventOrNull?.keyCode == java.awt.event.KeyEvent.VK_ALT &&
        keyEvent.type == KeyEventType.KeyUp

internal fun isDeleteEvent(keyEvent: KeyEvent) =
    keyEvent.awtEventOrNull?.keyCode == java.awt.event.KeyEvent.VK_DELETE ||
        keyEvent.awtEventOrNull?.keyCode == java.awt.event.KeyEvent.VK_BACK_SPACE

internal fun isSelectAllEvent(keyEvent: KeyEvent) =
    keyEvent.isMetaPressed &&
        keyEvent.awtEventOrNull?.keyCode == java.awt.event.KeyEvent.VK_A &&
        keyEvent.type == KeyEventType.KeyUp

internal fun isBoldEvent(keyEvent: KeyEvent) =
    keyEvent.isMetaPressed &&
        keyEvent.awtEventOrNull?.keyCode == java.awt.event.KeyEvent.VK_B &&
        keyEvent.type == KeyEventType.KeyUp

internal fun isItalicEvent(keyEvent: KeyEvent) =
    keyEvent.isMetaPressed &&
        keyEvent.awtEventOrNull?.keyCode == java.awt.event.KeyEvent.VK_I &&
        keyEvent.type == KeyEventType.KeyUp

internal fun isUnderlineEvent(keyEvent: KeyEvent) =
    keyEvent.isMetaPressed &&
        keyEvent.awtEventOrNull?.keyCode == java.awt.event.KeyEvent.VK_U &&
        keyEvent.type == KeyEventType.KeyUp

internal fun isLinkEvent(keyEvent: KeyEvent) =
    keyEvent.isMetaPressed &&
        keyEvent.awtEventOrNull?.keyCode == java.awt.event.KeyEvent.VK_L &&
        keyEvent.type == KeyEventType.KeyUp

internal fun isLocalSaveEvent(keyEvent: KeyEvent) =
    keyEvent.isMetaPressed &&
        keyEvent.isShiftPressed &&
        keyEvent.awtEventOrNull?.keyCode == java.awt.event.KeyEvent.VK_S &&
        keyEvent.type == KeyEventType.KeyUp

internal fun isBoxEvent(keyEvent: KeyEvent) =
    keyEvent.isMetaPressed &&
        keyEvent.isShiftPressed &&
        keyEvent.awtEventOrNull?.keyCode == java.awt.event.KeyEvent.VK_B &&
        keyEvent.type == KeyEventType.KeyUp
