package io.writeopia.sdk.backstack

/**
 * BackstackHandler is responsible for undoing and redoing content edition action (like deleting
 * a line, writing some text...).
 */
interface BackstackHandler {

    /**
     * Undo the last edition, if available
     */
    fun undo()

    /**
     * Redo the last undo call, if available
     */
    fun redo()
}
