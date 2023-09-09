package com.github.leandroborgesferreira.storyteller.backstack

import kotlinx.coroutines.flow.StateFlow

/**
 * Notifies is it possible to undo or redo actions. Classes that implement this interface are useful
 * to notify the UI that a button of undo/redo should or not be highlighted.
 */
interface BackstackInform {

    /**
     * Notifies if it is possible to undo an action.
     */
    val canUndo: StateFlow<Boolean>

    /**
     * Notifies if it is possible to redo an action.
     */
    val canRedo: StateFlow<Boolean>
}
