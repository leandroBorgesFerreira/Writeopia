package br.com.leandroferreira.storyteller.backstack

import br.com.leandroferreira.storyteller.model.backtrack.AddText
import br.com.leandroferreira.storyteller.model.change.TextEditInfo
import org.junit.Assert.*
import org.junit.Test

class BackStackManagerTest {


    private val backStackManager = BackStackManager()

    @Test
    fun `when adding text it should backstack the last action`() {
        val text = "hey, this is a text!"

        backStackManager.addAction(
            TextEditInfo(text, position = 1)
        )

        val backAction = backStackManager.undo()

        assertEquals(AddText(text, position = 1, isComplete = true), backAction)
    }

    @Test
    fun `when adding text character by character, it should merge it to a word`() {
        val text = "hey!"

        val editInfoList = text.map { char ->
            TextEditInfo(char.toString(), position = 1)
        }

        editInfoList.forEach(backStackManager::addAction)

        val addText = backStackManager.peek()
        assertEquals("The text should be merged again", text, (addText as AddText).text)
    }

    @Test
    fun `when adding text character by character, it should backstack the last word`() {
        val editInfoList = "hey, this is a text!".map { char ->
            TextEditInfo(char.toString(), position = 1)
        }

        editInfoList.forEach(backStackManager::addAction)

        val backAction = backStackManager.undo()
        assertEquals(AddText("text!", position = 1, isComplete = false), backAction)
    }

    @Test
    fun `when there is many spaces the class should work correctly`() {
        val editInfoList = "hey,       you!".map { char ->
            TextEditInfo(char.toString(), position = 1)
        }

        editInfoList.forEach(backStackManager::addAction)

        assertEquals(
            AddText("you!", position = 1, isComplete = false),
            backStackManager.undo()
        )
        assertEquals(
            AddText("hey,       ", position = 1, isComplete = true),
            backStackManager.undo()
        )
    }

    @Test
    fun `when adding text character by character, it should backstack the last word - many times`() {
        val editInfoList = "hey, this is a text!".map { char ->
            TextEditInfo(char.toString(), position = 1)
        }

        editInfoList.forEach(backStackManager::addAction)

        assertEquals(
            AddText("text!", position = 1, isComplete = false),
            backStackManager.undo()
        )
        assertEquals(AddText("a ", position = 1, isComplete = true), backStackManager.undo())
        assertEquals(AddText("is ", position = 1, isComplete = true), backStackManager.undo())
        assertEquals(
            AddText("this ", position = 1, isComplete = true),
            backStackManager.undo()
        )
        assertEquals(
            AddText("hey, ", position = 1, isComplete = true),
            backStackManager.undo()
        )
    }

    @Test
    fun `when adding text expanding the current text, the manager should work correctly`() {

    }
}
