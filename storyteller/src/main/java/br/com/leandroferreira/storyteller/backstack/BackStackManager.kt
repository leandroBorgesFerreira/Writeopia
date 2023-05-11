package br.com.leandroferreira.storyteller.backstack

import br.com.leandroferreira.storyteller.model.change.TextEditInfo
import java.util.Stack

private const val DEFAULT_BACK_TEXT_LIMIT = 10

class BackStackManager(private val backTextLimit: Int = DEFAULT_BACK_TEXT_LIMIT) {


    private val backStack: Stack<Any> = Stack()
    private val preStack: Stack<Any> = Stack()
    private val forwardStack: Stack<Any> = Stack()

    private var currentTextPosition: Int? = null

    fun addAction(action: Any) {
        forwardStack.clear()
        backStack.add(action)
    }

    fun backAction(): Any = backStack.pop().also(forwardStack::add)

    fun forwardAction(): Any = forwardStack.pop().also(backStack::add)

    /**
     * This method searches for the last action in the backStack. When the first item is a whole
     * action, like move, delete and merge information, it simply takes the first element and pops
     * it. But when the first items of the stack and TextEditInfo, it search for the last typed
     * word and pops all last word as an action.
     */
    private fun popAction(): Any {
        if (backStack.peek() !is TextEditInfo) return backStack.pop()

        val lastActions = mutableListOf<TextEditInfo>()
        val lastPosition = (backStack.peek() as TextEditInfo).position

        var acc = 0

        while (shouldContinueSearch(backStack.peek(), lastPosition) && acc < backTextLimit) {
            lastActions.add(backStack.pop() as TextEditInfo)
            acc++
        }

        val textToErase = lastActions.joinToString(separator = "")
        return TextEditInfo(text = textToErase, position = lastPosition)
    }

    private fun shouldContinueSearch(nextElement: Any, lastPosition: Int): Boolean =
        nextElement is TextEditInfo &&
            nextElement.position == lastPosition &&
            !nextElement.text.contains(" ")
}
