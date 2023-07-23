package com.github.leandroborgesferreira.storyteller.backstack

import com.github.leandroborgesferreira.storyteller.model.action.Action
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Stack

private const val HAS_SPACE_REGEX = "\\s\\S"

class PerActionBackstackManager : BackstackInform {

    private val backStack: Stack<Action> = Stack()
    private val forwardStack: Stack<Action> = Stack()

    private val _canUndo = MutableStateFlow(false)
    private val _canRedo = MutableStateFlow(false)

    override val canUndo: StateFlow<Boolean> = _canUndo
    override val canRedo: StateFlow<Boolean> = _canRedo

    fun undo(): Action = backStack.pop().also { action ->
        forwardStack.add(action)
        _canUndo.value = backStack.isNotEmpty()
        _canRedo.value = forwardStack.isNotEmpty()
    }

    fun redo(): Action = forwardStack.pop().also { action ->
        backStack.add(action)
        _canUndo.value = backStack.isNotEmpty()
        _canRedo.value = forwardStack.isNotEmpty()
    }

    /**
     * Add action evaluates if the action is a whole action, like move, delete, merge content or
     * a non whole action, like editing a text. If the action is not whole, it merges the last
     * action with the new one in the stack. If the action is whole, it simply adds it to the
     * backstack.
     */
    fun addAction(action: Action) {
        when {
            backStack.isEmpty() -> {
                addActionWhenEmpty(action)
            }

            action !is Action.TextEdit -> {
                addWhenWholeAction(action)
            }

            else -> {
                handleNewTextAdded(action)
            }
        }

        forwardStack.clear()
        _canUndo.value = backStack.isNotEmpty()
        _canRedo.value = forwardStack.isNotEmpty()
    }

    fun peek(): Action = backStack.peek()

    private fun addActionWhenEmpty(action: Action) {
        if (action is Action.TextEdit) {
            backStack.add(
                Action.AddText(
                    text = action.text,
                    position = action.position,
                    isComplete = action.text.contains(HAS_SPACE_REGEX.toRegex())
                )
            )
        } else {
            backStack.add(action)
        }
    }

    private fun addWhenWholeAction(action: Action) {
        val lastAction = backStack.peek()
        if (lastAction is Action.AddText && !lastAction.isComplete) {
            val popped = backStack.pop()
            backStack.add((popped as Action.AddText).copy(isComplete = true))
        }

        backStack.add(action)
    }

    private fun handleNewTextAdded(action: Action.TextEdit) {
        val lastAction = backStack.peek()

        if (lastAction is Action.AddText &&
            !lastAction.isComplete &&
            action.position == lastAction.position
        ) {
            val popped = backStack.pop() as Action.AddText
            val newText = "${popped.text}${action.text}"
            val isComplete = newText.contains(HAS_SPACE_REGEX.toRegex())

            if (!isComplete) {
                val newAction = popped.copy(
                    text = newText,
                    isComplete = false
                )
                backStack.add(newAction)
            } else {
                val newAction = popped.copy(isComplete = true)
                backStack.add(newAction)
                backStack.add(
                    Action.AddText(
                        text = action.text,
                        position = action.position,
                        isComplete = false
                    )
                )
            }
        } else {
            //Todo: Review this whole method!
            backStack.add(
                Action.AddText(
                    text = action.text,
                    position = action.position,
                    isComplete = false
                )
            )
        }
    }
}
