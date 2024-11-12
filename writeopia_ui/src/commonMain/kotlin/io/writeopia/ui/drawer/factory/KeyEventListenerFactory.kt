package io.writeopia.ui.drawer.factory

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.input.TextFieldValue
import io.writeopia.sdk.model.action.Action
import io.writeopia.sdk.models.command.TypeInfo
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.ui.manager.WriteopiaStateManager
import io.writeopia.ui.model.EmptyErase

////
object KeyEventListenerFactory {

    fun desktop(
        manager: WriteopiaStateManager,
    ): (KeyEvent, TextFieldValue, StoryStep, Int, EmptyErase, Int, Boolean) -> Boolean {
        return { keyEvent, inputText, step, position, onEmptyErase, cursorPosition, isInLastLine ->
            when {
                isEmptyErase(keyEvent, inputText) -> {
                    when (onEmptyErase) {
                        EmptyErase.DELETE -> {
                            manager.onErase(Action.EraseStory(step, position))
                        }

                        EmptyErase.CHANGE_TYPE -> {
                            manager.changeStoryType(position, TypeInfo(StoryTypes.TEXT.type), null)
                        }

                        EmptyErase.DISABLED -> {}
                    }

                    true
                }

                isMoveUpEventEnd(keyEvent) -> {
                    manager.moveToPrevious(cursor = cursorPosition)
                    true
                }

                isMoveStrongUpEvent(keyEvent) -> {
                    manager.moveToPrevious(cursor = cursorPosition, 10)
                    true
                }

                isMoveDownEventEnd(keyEvent) && isInLastLine -> {
                    manager.moveToNext(cursor = cursorPosition)
                    true
                }

                isMoveStrongDownEvent(keyEvent) && isInLastLine -> {
                    manager.moveToNext(cursor = cursorPosition, 10)
                    true
                }

                else -> false
            }
        }
    }

    fun android(
        manager: WriteopiaStateManager,
        isEmptyErase: (KeyEvent, TextFieldValue) -> Boolean = { _, _ -> false },
    ): (KeyEvent, TextFieldValue, StoryStep, Int, EmptyErase, Int, Boolean) -> Boolean {
        return { keyEvent, inputText, step, position, onEmptyErase, cursorPosition, isInLastLine ->
            when {
                isEmptyErase(keyEvent, inputText) -> {
                    when (onEmptyErase) {
                        EmptyErase.DELETE -> {
                            manager.onErase(Action.EraseStory(step, position))
                        }

                        EmptyErase.CHANGE_TYPE -> {
                            manager.changeStoryType(position, TypeInfo(StoryTypes.TEXT.type), null)
                        }

                        EmptyErase.DISABLED -> {}


                    }

                    true
                }

                isMoveUpEventEnd(keyEvent) -> {
                    manager.moveToPrevious(cursor = cursorPosition)
                    true
                }

                isMoveStrongUpEvent(keyEvent) -> {
                    manager.moveToPrevious(cursor = cursorPosition, 10)
                    true
                }

                isMoveDownEventEnd(keyEvent) && isInLastLine -> {
                    manager.moveToNext(cursor = cursorPosition)
                    true
                }

                isMoveStrongDownEvent(keyEvent) && isInLastLine -> {
                    manager.moveToNext(cursor = cursorPosition, 10)
                    true
                }

                else -> false
            }
        }
    }

    fun js(
        manager: WriteopiaStateManager,
    ): (KeyEvent, TextFieldValue, StoryStep, Int, EmptyErase, Int, Boolean) -> Boolean {
        return { keyEvent, inputText, step, position, onEmptyErase, _, _ ->
            when {
                isLineBreak(keyEvent) -> {
                    manager.onLineBreak(Action.LineBreak(step, position = position))
                    true
                }

                isEmptyErase(keyEvent, inputText) -> {
                    when (onEmptyErase) {
                        EmptyErase.DELETE -> {
                            manager.onErase(Action.EraseStory(step, position))
                        }

                        EmptyErase.CHANGE_TYPE -> {
                            manager.changeStoryType(position, TypeInfo(StoryTypes.TEXT.type), null)
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

private fun isEmptyErase(
    keyEvent: KeyEvent,
    input: TextFieldValue
): Boolean = keyEvent.key == Key.Backspace
    && keyEvent.type == KeyEventType.KeyUp
    && input.selection.start == 0

private fun isMoveUpEventEnd(keyEvent: KeyEvent) =
    keyEvent.key == Key.DirectionUp
        && keyEvent.type == KeyEventType.KeyDown
        && !keyEvent.isMetaPressed

private fun isMoveDownEventEnd(keyEvent: KeyEvent) =
    keyEvent.key == Key.DirectionDown
        && keyEvent.type == KeyEventType.KeyDown
        && !keyEvent.isMetaPressed

private fun isMoveStrongUpEvent(keyEvent: KeyEvent) =
    keyEvent.key == Key.DirectionUp
        && keyEvent.type == KeyEventType.KeyDown
        && keyEvent.isMetaPressed

private fun isMoveStrongDownEvent(keyEvent: KeyEvent) =
    keyEvent.key == Key.DirectionDown
        && keyEvent.type == KeyEventType.KeyDown
        && keyEvent.isMetaPressed

private fun isLineBreak(keyEvent: KeyEvent): Boolean = keyEvent.key == Key.Enter

