package io.writeopia.sdk.drawer.factory

import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.text.input.TextFieldValue
import io.writeopia.sdk.manager.WriteopiaManager
import io.writeopia.sdk.model.action.Action
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import org.jetbrains.skiko.SkikoKey

object KeyEventListenerFactoryDesktop {

    fun createDesktop(
        manager: WriteopiaManager,
        deleteOnEmptyErase: Boolean = false
    ): (KeyEvent, TextFieldValue, StoryStep, Int) -> Boolean =
        KeyEventListenerFactory.create(
            manager,
            isEmptyErase = { keyEvent, inputText ->
                keyEvent.awtEventOrNull?.keyCode == java.awt.event.KeyEvent.VK_BACK_SPACE &&
                        inputText.selection.start == 0
            },
            deleteOnEmptyErase = deleteOnEmptyErase
        )
}