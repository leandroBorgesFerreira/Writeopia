package com.github.leandroferreira.storyteller.drawer.modifier

import android.view.KeyEvent
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextRange

fun Modifier.callOnEmptyErase(textRange: TextRange, callback: () -> Unit): Modifier =
    onKeyEvent { keyEvent ->
        if (keyEvent.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_DEL && textRange.start == 0) {
            callback()
            true
        } else {
            false
        }
    }
