package io.writeopia.ui.drawer.factory

import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.text.input.TextFieldValue
import io.writeopia.sdk.manager.WriteopiaManager
import io.writeopia.sdk.model.action.Action
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes

object KeyEventListenerFactory {

    fun create(
        manager: WriteopiaManager,
        isEmptyErase: (KeyEvent, TextFieldValue) -> Boolean = { _, _ -> false },
        deleteOnEmptyErase: Boolean = false
    ): (KeyEvent, TextFieldValue, StoryStep, Int) -> Boolean {
        return { keyEvent, inputText, step, position ->
            when {
                isEmptyErase(keyEvent, inputText) -> {
                    if (deleteOnEmptyErase) {
                        manager.onDelete(Action.DeleteStory(step, position))
                    } else {
                        manager.changeStoryType(position, StoryTypes.TEXT.type, null)
                    }

                    true
                }

                else -> false
            }
        }
    }

    fun js(
        manager: WriteopiaManager,
        isLineBreakKey: (KeyEvent) -> Boolean = { false },
        isEmptyErase: (KeyEvent, TextFieldValue) -> Boolean = { _, _ -> false },
        deleteOnEmptyErase: Boolean = false
    ): (KeyEvent, TextFieldValue, StoryStep, Int) -> Boolean {
        return { keyEvent, inputText, step, position ->
            when {
                isLineBreakKey(keyEvent) -> {
                    manager.onLineBreak(Action.LineBreak(step, position = position))
                    true
                }

                isEmptyErase(keyEvent, inputText) -> {
                    if (deleteOnEmptyErase) {
                        manager.onDelete(Action.DeleteStory(step, position))
                    } else {
                        manager.changeStoryType(position, StoryTypes.TEXT.type, null)
                    }

                    true
                }

                else -> false
            }
        }
    }
}