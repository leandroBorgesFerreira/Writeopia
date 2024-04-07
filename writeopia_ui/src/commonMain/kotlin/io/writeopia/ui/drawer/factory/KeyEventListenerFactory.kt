package io.writeopia.ui.drawer.factory

import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import androidx.compose.ui.text.input.TextFieldValue
import io.writeopia.ui.manager.WriteopiaStateManager
import io.writeopia.sdk.model.action.Action
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.ui.model.EmptyErase

object KeyEventListenerFactory {

    fun create(
        manager: WriteopiaStateManager,
        isEmptyErase: (KeyEvent, TextFieldValue) -> Boolean = { _, _ -> false },
    ): (KeyEvent, TextFieldValue, StoryStep, Int, EmptyErase) -> Boolean {
        return { keyEvent, inputText, step, position, onEmptyErase ->
            when {
                isEmptyErase(keyEvent, inputText) -> {
                    when (onEmptyErase) {
                        EmptyErase.DELETE -> {
                            manager.onDelete(Action.DeleteStory(step, position))
                        }

                        EmptyErase.CHANGE_TYPE -> {
                            manager.changeStoryType(position, StoryTypes.TEXT.type, null)
                        }

                        EmptyErase.DISABLED -> {}
                    }

                    true
                }

                else -> false
            }
        }
    }

    fun js(
        manager: WriteopiaStateManager,
        isLineBreak: (KeyEvent) -> Boolean = { false },
        isEmptyErase: (KeyEvent, TextFieldValue) -> Boolean = { _, _ -> false },
    ): (KeyEvent, TextFieldValue, StoryStep, Int, EmptyErase) -> Boolean {
        return { keyEvent, inputText, step, position, onEmptyErase ->
            println("Received JS key event. inputText.text: ${inputText.text}, keyCode: ${keyEvent.key.keyCode}, key: ${keyEvent.nativeKeyEvent}")

            when {
                isLineBreak(keyEvent) -> {
                    manager.onLineBreak(Action.LineBreak(step, position = position))
                    true
                }

                isEmptyErase(keyEvent, inputText) -> {
                    when (onEmptyErase) {
                        EmptyErase.DELETE -> {
                            manager.onDelete(Action.DeleteStory(step, position))
                        }

                        EmptyErase.CHANGE_TYPE -> {
                            manager.changeStoryType(position, StoryTypes.TEXT.type, null)
                        }

                        EmptyErase.DISABLED -> {}
                    }

                    true
                }

                else -> false
            }
        }
    }
}