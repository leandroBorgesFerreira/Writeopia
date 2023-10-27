package io.writeopia.sdk.drawer.factory

import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.text.input.TextFieldValue
import io.writeopia.sdk.manager.WriteopiaManager
import io.writeopia.sdk.models.story.StoryStep

object KeyEventListenerFactoryDesktop {

    fun createDesktop(
        manager: WriteopiaManager,
        deleteOnEmptyErase: Boolean = false
    ): (KeyEvent, TextFieldValue, StoryStep, Int) -> Boolean =
        KeyEventListenerFactory.desktop(
            manager,
            isEmptyErase = { keyEvent, inputText ->
                keyEvent.awtEventOrNull?.keyCode == java.awt.event.KeyEvent.VK_BACK_SPACE &&
                        inputText.selection.start == 0
            },
            deleteOnEmptyErase = deleteOnEmptyErase
        )
}