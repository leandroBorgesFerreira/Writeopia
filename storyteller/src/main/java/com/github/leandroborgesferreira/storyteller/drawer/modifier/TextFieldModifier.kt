package com.github.leandroborgesferreira.storyteller.drawer.modifier

import android.view.KeyEvent
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextRange

/**
 * Call the [callback] function when the a erase key was pressed and there isn't any available text.
 */
internal fun Modifier.callOnEmptyErase(textRange: TextRange, callback: () -> Unit): Modifier =
    onKeyEvent { keyEvent ->
        if (keyEvent.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_DEL && textRange.start == 0) {
            callback()
            true
        } else {
            false
        }
    }
