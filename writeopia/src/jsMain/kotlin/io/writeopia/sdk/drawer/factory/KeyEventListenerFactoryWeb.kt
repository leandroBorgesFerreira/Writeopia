package io.writeopia.sdk.drawer.factory

import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.text.input.TextFieldValue
import io.writeopia.sdk.manager.WriteopiaManager
import io.writeopia.sdk.models.story.StoryStep
import org.jetbrains.skiko.SkikoKey

object KeyEventListenerFactoryWeb {

    fun createWeb(
        manager: WriteopiaManager,
        deleteOnEmptyErase: Boolean = false
    ): (KeyEvent, TextFieldValue, StoryStep, Int) -> Boolean =
        KeyEventListenerFactory.create(
            manager,
            isLineBreakKey = { keyEvent ->
                keyEvent.nativeKeyEvent.key == SkikoKey.KEY_ENTER
            },
            isEmptyErase = { keyEvent, inputText ->
                keyEvent.nativeKeyEvent.key == SkikoKey.KEY_BACKSPACE && inputText.selection.start == 0
            },
            deleteOnEmptyErase = deleteOnEmptyErase
        )
}