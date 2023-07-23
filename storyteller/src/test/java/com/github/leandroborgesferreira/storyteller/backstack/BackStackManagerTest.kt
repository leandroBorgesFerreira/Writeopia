package com.github.leandroborgesferreira.storyteller.backstack

import com.github.leandroborgesferreira.storyteller.model.action.Action
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test

class BackStackManagerTest {


    private val backStackManager = PerActionBackstackManager()

    @Test
    fun `when adding text it should backstack the last action`() {
        val text = "hey, this is a text!"

        backStackManager.addAction(
            Action.TextEdit(text, position = 1)
        )

        val backAction = backStackManager.undo()

        assertEquals(Action.AddText(text, position = 1, isComplete = true), backAction)
    }

    @Test
    fun `when adding text character by character, it should merge it to a word`() {
        val text = "hey!"

        val editInfoList = text.map { char ->
            Action.TextEdit(char.toString(), position = 1)
        }

        editInfoList.forEach(backStackManager::addAction)

        val addText = backStackManager.peek()
        assertEquals("The text should be merged again", text, (addText as Action.AddText).text)
    }

    @Test
    fun `when adding text character by character, it should backstack the last word`() {
        val editInfoList = "hey, this is a text!".map { char ->
            Action.TextEdit(char.toString(), position = 1)
        }

        editInfoList.forEach(backStackManager::addAction)

        val backAction = backStackManager.undo()
        assertEquals(Action.AddText("text!", position = 1, isComplete = false), backAction)
    }

    @Test
    fun `when there is many spaces the class should work correctly`() {
        val editInfoList = "hey,       you!".map { char ->
            Action.TextEdit(char.toString(), position = 1)
        }

        editInfoList.forEach(backStackManager::addAction)

        assertEquals(
            Action.AddText("you!", position = 1, isComplete = false),
            backStackManager.undo()
        )
        assertEquals(
            Action.AddText("hey,       ", position = 1, isComplete = true),
            backStackManager.undo()
        )
    }

    @Test
    fun `when adding text character by character, it should backstack the last word - many times`() {
        val editInfoList = "hey, this is a text!".map { char ->
            Action.TextEdit(char.toString(), position = 1)
        }

        editInfoList.forEach(backStackManager::addAction)

        assertEquals(
            Action.AddText("text!", position = 1, isComplete = false),
            backStackManager.undo()
        )
        assertEquals(Action.AddText("a ", position = 1, isComplete = true), backStackManager.undo())
        assertEquals(Action.AddText("is ", position = 1, isComplete = true), backStackManager.undo())
        assertEquals(
            Action.AddText("this ", position = 1, isComplete = true),
            backStackManager.undo()
        )
        assertEquals(
            Action.AddText("hey, ", position = 1, isComplete = true),
            backStackManager.undo()
        )
    }

    @Test
    @Ignore
    fun `when adding text expanding the current text, the manager should work correctly`() {

    }
}
