package com.github.leandroborgesferreira.storyteller.manager

import com.github.leandroborgesferreira.storyteller.model.action.Action
import com.github.leandroborgesferreira.storyteller.models.document.Document
import com.github.leandroborgesferreira.storyteller.models.story.StoryStep
import com.github.leandroborgesferreira.storyteller.model.story.StoryTypes
import com.github.leandroborgesferreira.storyteller.repository.StoriesRepository
import com.github.leandroborgesferreira.storyteller.utils.MainDispatcherRule
import com.github.leandroborgesferreira.storyteller.utils.MapStoryData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import java.util.Stack

class StoryTellerManagerTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val imagesInLineRepo: StoriesRepository = object : StoriesRepository {
        override suspend fun history(): Map<Int, StoryStep> = MapStoryData.imageStepsList()
    }

    private val imageGroupRepo: StoriesRepository = object : StoriesRepository {
        override suspend fun history(): Map<Int, StoryStep> = MapStoryData.imageGroup()
    }

    private val messagesRepo: StoriesRepository = object : StoriesRepository {
        override suspend fun history(): Map<Int, StoryStep> = MapStoryData.messagesInLine()
    }

    private val singleMessageRepo: StoriesRepository = object : StoriesRepository {
        override suspend fun history(): Map<Int, StoryStep> = MapStoryData.singleMessage()
    }

    private val complexMessagesRepository: StoriesRepository = object : StoriesRepository {
        override suspend fun history(): Map<Int, StoryStep> = MapStoryData.syncHistory()
    }

    @Test
    fun `a new story should start correctly`() {
        val manager = StoryTellerManager(dispatcher = Dispatchers.Main, userId = { "" })

        manager.newStory()

        val currentStory = manager.currentStory.value.stories
        val expected = mapOf(
            0 to StoryStep(type = StoryTypes.TITLE.type),
            1 to StoryStep(type = StoryTypes.SPACE.type),
            2 to StoryStep(type = StoryTypes.LARGE_SPACE.type),
        ).mapValues { (_, storyStep) ->
            storyStep.type
        }

        assertEquals(
            expected,
            currentStory.mapValues { (_, storyStep) -> storyStep.type }
        )
    }

    @Test
    fun `one space has to be added between steps`() = runTest {
        val storyManager = StoryTellerManager(dispatcher = Dispatchers.Main, userId = { "" })
        val oldSize = messagesRepo.history().size

        storyManager.initDocument(Document(content = messagesRepo.history(), userId = ""))

        val newStory = storyManager.currentStory.value.stories

        assertEquals("a space should be added between each step", oldSize * 2 + 1, newStory.size)
    }

    @Test
    fun `when a line break happens, one (and only one) new item should be created`() {
        val input = MapStoryData.singleCheckItem()
        val checkItem = input[0]

        val storyManager =
            StoryTellerManager(dispatcher = Dispatchers.Main, userId = { "" }).apply {
                initDocument(Document(content = input, userId = ""))
            }

        val currentStory = storyManager.currentStory.value.stories

        storyManager.onLineBreak(
            Action.LineBreak(storyStep = checkItem!!, position = 0)
        )

        val newStory = storyManager.currentStory.value.stories

        assertEquals("the first item should be a check_item", "check_item", newStory[0]!!.type.name)
        assertEquals(
            "the second item should be a check_item",
            "check_item",
            newStory[2]!!.type.name
        )
        assertEquals("the size of the story should be 5", currentStory.size + 2, newStory.size)
    }

    @Test
    fun `merge request should work`() = runTest {
        val storyManager = StoryTellerManager(dispatcher = Dispatchers.Main, userId = { "" })
        storyManager.initDocument(Document(content = imagesInLineRepo.history(), userId = ""))

        val currentStory = storyManager.currentStory.value.stories
        val initialSize = currentStory.size

        val positionFrom = 2
        val positionTo = 0
        val sender = currentStory[positionFrom]!!
        val receiver = currentStory[positionTo]!!

        assertFalse("The first step is not a Group", currentStory[positionTo]!!.isGroup)

        storyManager.mergeRequest(
            Action.Merge(
                sender = sender,
                receiver = receiver,
                positionFrom = positionFrom,
                positionTo = positionTo,
            )
        )

        val newStory = storyManager.currentStory.value.stories

        assertEquals(
            "One image should be removed and one space",
            initialSize - 2,
            newStory.size
        )
        assertEquals(
            "The first step should be now a GroupStep",
            true,
            newStory[positionTo]?.isGroup
        )
    }

    @Test
    fun `merge request should work2`() = runTest {
        val storyManager = StoryTellerManager(dispatcher = Dispatchers.Main, userId = { "" })
        storyManager.initDocument(Document(content = imagesInLineRepo.history(), userId = ""))

        val currentStory = storyManager.currentStory.value.stories
        val initialSize = currentStory.size

        val positionFrom = 2
        val positionTo = 0

        storyManager.mergeRequest(
            Action.Merge(
                receiver = currentStory[positionTo]!!,
                sender = currentStory[positionFrom]!!,
                positionTo = positionTo,
                positionFrom = positionFrom
            )
        )

        val newStory = storyManager.currentStory.value.stories

        assertEquals(initialSize - 2, newStory.size)
        assertEquals(
            "The first image should be a GroupImage now",
            true,
            newStory[positionTo]?.isGroup
        )
        assertTrue("Other images should still exist", newStory[positionFrom] is StoryStep)
    }

    @Test
    fun `multiple merge requests should work`() = runTest {
        val storyManager = StoryTellerManager(dispatcher = Dispatchers.Main, userId = { "" })
        storyManager.initDocument(Document(content = imagesInLineRepo.history(), userId = ""))

        val currentStory = storyManager.currentStory.value.stories
        val initialSize = currentStory.size

        val positionFrom = 2
        val positionTo = 0

        storyManager.mergeRequest(
            Action.Merge(
                receiver = currentStory[positionTo]!!,
                sender = currentStory[positionFrom]!!,
                positionTo = positionTo,
                positionFrom = positionFrom
            )
        )

        val newHistory = storyManager.currentStory.value.stories

        assertEquals("One space and one image were removed", initialSize - 2, newHistory.size)
        assertEquals(
            "The first message should be a GroupImage instead of a Image now",
            true,
            newHistory[positionTo]?.isGroup
        )
        assertEquals(
            "The new created GroupImage should have 2 images",
            2,
            newHistory[positionTo]!!.steps.size
        )

        repeat(2) {
            val newHistory2 = storyManager.currentStory.value.stories

            val newPositionFrom = 2
            val newPositionTo = 0

            storyManager.mergeRequest(
                Action.Merge(
                    receiver = newHistory2[newPositionTo]!!,
                    sender = newHistory2[newPositionFrom]!!,
                    positionTo = newPositionTo,
                    positionFrom = newPositionFrom
                )
            )
        }

        val newHistory3 = storyManager.currentStory.value.stories

        assertEquals(
            "The minimum side should be 4 (group, space, large_space)",
            3,
            newHistory3.size
        )
        assertEquals("The GroupImage should still exist", true, newHistory3[positionTo]?.isGroup)
        assertEquals(
            "Now the group has 3 images",
            3,
            newHistory3[positionTo]!!.steps.size
        )
    }

    @Test
    fun `it should be possible to merge an image inside a message group`() = runTest {
        val storyManager = StoryTellerManager(dispatcher = Dispatchers.Main, userId = { "" })
        storyManager.initDocument(Document(content = imageGroupRepo.history(), userId = ""))

        val currentStory = storyManager.currentStory.value.stories
        val initialSize = currentStory.size
        val initialImageGroupSize = currentStory[0]!!.steps.size

        val positionFrom = 2
        val positionTo = 0

        val mergeInfo = Action.Merge(
            receiver = currentStory[positionTo]!!,
            sender = currentStory[positionFrom]!!,
            positionTo = positionTo,
            positionFrom = positionFrom
        )
        storyManager.mergeRequest(mergeInfo)

        val newStory = storyManager.currentStory.value.stories

        assertEquals("One image and one space were removed", initialSize - 2, newStory.size)
        assertTrue(newStory[positionTo]?.isGroup == true)
        assertEquals(
            "One element was added to the GroupStep",
            initialImageGroupSize + 1,
            newStory[positionTo]!!.steps.size
        )
    }

    @Test
    fun `it should be possible to merge an image outside a message group`() = runTest {
        val storyManager = StoryTellerManager(dispatcher = Dispatchers.Main, userId = { "" })
        storyManager.initDocument(Document(content = imageGroupRepo.history(), userId = ""))
        val positionTo = 2
        val positionFrom = 0

        val currentStory = storyManager.currentStory.value.stories
        val initialGroupSize = currentStory[positionFrom]!!.steps.size

        storyManager.mergeRequest(
            Action.Merge(
                receiver = currentStory[positionTo]!!,
                sender = currentStory[positionFrom]!!.steps[0],
                positionTo = positionTo,
                positionFrom = positionFrom
            )
        )

        val newStory = storyManager.currentStory.value.stories

        assertEquals(
            "The image should be now in the position 3, because of spaces.",
            StoryTypes.GROUP_IMAGE.type,
            newStory[positionTo]!!.type
        )
        assertEquals(
            "The new story now it the GroupImage",
            initialGroupSize - 1,
            newStory[positionFrom]!!.steps.size
        )
    }

    @Test
    @Ignore
    fun `when moving outside of a group, the parent Id should be null now`() = runTest {
        val storyManager = StoryTellerManager(dispatcher = Dispatchers.Main, userId = { "" })
        storyManager.initDocument(Document(content = imageGroupRepo.history(), userId = ""))

        val currentStory = storyManager.currentStory.value.stories

        val positionTo = currentStory.size - 2
        val positionFrom = 1

        val storyToMove = currentStory[positionFrom]!!.steps[0]

        storyManager.moveRequest(
            Action.Move(
                storyStep = storyToMove,
                positionTo = positionTo,
                positionFrom = positionFrom
            )
        )

        val newStory = storyManager.currentStory.value.stories
        val lastContentStep = newStory[newStory.size - 3]!!

        assertEquals(
            "The last StoryUnit should be an image.",
            StoryTypes.IMAGE.type,
            lastContentStep.type
        )
        assertEquals(
            "The image should be in the correct place now.",
            storyToMove.id,
            lastContentStep.id
        )
        assertNull(
            "The parent of the separated image, should not be there.",
            lastContentStep.parentId
        )
        assertFalse(
            "The moved image should not be in the group anymore",
            newStory[positionFrom]!!.steps.any { storyUnit ->
                storyUnit.id == storyToMove.id
            })
    }

    @Test
    fun `it should be possible to switch message places`() = runTest {
        val simpleMessagesRepo: StoriesRepository = object : StoriesRepository {
            override suspend fun history(): Map<Int, StoryStep> = MapStoryData.simpleMessages()
        }


        val storyManager = StoryTellerManager(dispatcher = Dispatchers.Main, userId = { "" })
        storyManager.initDocument(Document(content = simpleMessagesRepo.history(), userId = ""))

        val positionFrom = 0
        val positionTo = 3

        val currentStory = storyManager.currentStory.value.stories
        val storyUnitToMove = currentStory[positionFrom]!!

        storyManager.moveRequest(Action.Move(storyUnitToMove, positionFrom, positionTo))

        val newStory = storyManager.currentStory.value.stories

        assertEquals(
            "The first story should have been moved",
            newStory[positionTo]!!.text,
            storyUnitToMove.text
        )
    }

    @Test
    fun `it should be possible to revert move`() = runTest {
        val simpleMessagesRepo: StoriesRepository = object : StoriesRepository {
            override suspend fun history(): Map<Int, StoryStep> = MapStoryData.simpleMessages()
        }

        val storyManager = StoryTellerManager(dispatcher = Dispatchers.Main, userId = { "" })

        storyManager.initDocument(Document(content = simpleMessagesRepo.history(), userId = ""))

        val positionFrom = 2
        val positionTo = 5

        val currentStory = storyManager.currentStory.value.stories
        val storyUnitToMove = currentStory[positionFrom]!!

        storyManager.moveRequest(Action.Move(storyUnitToMove, positionFrom, positionTo))
        storyManager.undo()

        val oldIds = currentStory.mapValues { (_, storyStep) -> storyStep.text }
        val newIds =
            storyManager.currentStory.value.stories.mapValues { (_, storyStep) -> storyStep.text }

        assertEquals(oldIds, newIds)
    }

    @Test
    fun `deleting and leave a single element in a group destroys the group`() = runTest {
        val storyManager = StoryTellerManager(dispatcher = Dispatchers.Main, userId = { "" })
        storyManager.initDocument(Document(content = imageGroupRepo.history(), userId = ""))
        val groupPosition = 0

        val currentStory = storyManager.currentStory.value.stories
        assertEquals(
            "initial the story unit should be a group",
            StoryTypes.GROUP_IMAGE.type,
            currentStory[groupPosition]!!.type
        )

        val lastImageInsideGroup =
            { storyManager.currentStory.value.stories[groupPosition]!!.steps.last() }

        storyManager.onDelete(
            Action.DeleteStory(
                storyStep = lastImageInsideGroup(),
                position = groupPosition
            )
        )

        storyManager.onDelete(
            Action.DeleteStory(
                storyStep = lastImageInsideGroup(),
                position = groupPosition
            )
        )

        val newStory = storyManager.currentStory.value.stories

        assertEquals(
            "the group become just an image because there's only a single image",
            StoryTypes.IMAGE.type,
            newStory[groupPosition]!!.type
        )
    }

    @Test
    fun `when deleting a message it should not leave consecutive spaces`() = runTest {
        val storyManager = StoryTellerManager(dispatcher = Dispatchers.Main, userId = { "" })
        storyManager.initDocument(Document(content = messagesRepo.history(), userId = ""))

        storyManager.onDelete(
            Action.DeleteStory(
                storyManager.currentStory.value.stories[3]!!, 3
            )
        )

        val stack: Stack<StoryStep> = Stack()

        storyManager.currentStory.value.stories.forEach { (_, storyUnit) ->
            if (stack.isNotEmpty() && stack.peek().type.name == "space" && storyUnit.type.name == "space") {
                fail("Consecutive spaces happened.")
            }

            stack.add(storyUnit)
        }
    }

    @Test
    fun `when a line break happens, a new story unit with the same type should be created - simple`() =
        runTest {
            val storyManager = StoryTellerManager(dispatcher = Dispatchers.Main, userId = { "" })
            storyManager.initDocument(Document(content = singleMessageRepo.history(), userId = ""))

            val stories = storyManager.currentStory.value.stories
            val initialSize = stories.size
            val position = 0

            storyManager.onLineBreak(Action.LineBreak(stories[position]!!, position = position))

            assertEquals(
                "2 new stories should have been added",
                initialSize + 2,
                storyManager.currentStory.value.stories.size
            )
        }

    @Test
    fun `when a line break happens, a new story unit with the same type should be created - complex`() =
        runTest {
            val storyManager = StoryTellerManager(dispatcher = Dispatchers.Main, userId = { "" })
            storyManager.initDocument(Document(content = messagesRepo.history(), userId = ""))

            val stories = storyManager.currentStory.value.stories
            val initialSize = stories.size
            val breakPosition = 0

            storyManager.onLineBreak(Action.LineBreak(stories[breakPosition]!!, breakPosition))

            val newStory = storyManager.currentStory.value.stories

            assertEquals(
                "2 new stories should have been added",
                initialSize + 2,
                newStory.size
            )
        }

    @Test
    @Ignore
    fun `Complex move case1`() = runTest {
        /**
         * Steps:
         * 1 - Make 3 single images into a group
         * - Check that the 3 images are in a group
         * 2 - Move one image away.
         * - Check that the correct image was moved correctly
         */
        val storyManager = StoryTellerManager(dispatcher = Dispatchers.Main, userId = { "" })
        storyManager.initDocument(Document(content = complexMessagesRepository.history(), userId = ""))

        val stories = storyManager.currentStory.value.stories

        val positionTo = 1
        val positionFrom = 3
        storyManager.mergeRequest(
            Action.Merge(
                receiver = stories[positionTo]!!,
                sender = stories[positionFrom]!!,
                positionFrom = positionFrom,
                positionTo = positionTo,
            )
        )

        val newStory = storyManager.currentStory.value.stories

        assertEquals("The images should have been merged", 2, (newStory[1]!!).steps.size)

        val stories2 = storyManager.currentStory.value.stories

        val positionTo2 = 1
        val positionFrom2 = 3
        storyManager.mergeRequest(
            Action.Merge(
                receiver = (stories2[positionTo2]!!).steps.first(),
                sender = stories2[positionFrom2]!!,
                positionFrom = positionFrom2,
                positionTo = positionTo2,
            )
        )

        val newStory2 = storyManager.currentStory.value.stories

        assertEquals(
            "The images should have been merged",
            3,
            (newStory2[1]!!).steps.distinctBy { storyUnit -> storyUnit.localId }.size
        )

        val positionFrom3 = 1
        val positionTo3 = 4
        val storyToMove = (newStory[positionFrom3]!!).steps.first()
        storyManager.moveRequest(
            Action.Move(
                storyStep = storyToMove,
                positionFrom = positionFrom3,
                positionTo = positionTo3,
            )
        )

        val newStory3 = storyManager.currentStory.value.stories

        assertEquals(
            "One image should have been separated",
            2,
            (newStory3[1]!!).steps.size
        )
        assertEquals(
            "The correct StoryUnit should have been moved",
            storyToMove.id,
            newStory3[5]!!.id
        )
    }

    @Test
    fun `it should be possible to add content and undo it - 1 unit`() {
        val storyManager = StoryTellerManager(dispatcher = Dispatchers.Main, userId = { "" })
        val input = MapStoryData.singleCheckItem()

        storyManager.initDocument(Document(content = input, userId = ""))
        val currentStory = storyManager.currentStory.value.stories

        storyManager.onLineBreak(Action.LineBreak(input[0]!!, 0))
        storyManager.undo()
        val newStory = storyManager.currentStory.value.stories

        assertEquals(currentStory.size, newStory.size)
    }

    @Test
    fun `it should be possible to add content and undo it - many units`() = runTest {
        val storyManager = StoryTellerManager(dispatcher = Dispatchers.Main, userId = { "" })
        val input = MapStoryData.singleCheckItem()

        storyManager.initDocument(Document(content = input, userId = ""))
        val currentStory = storyManager.currentStory.value.stories

        storyManager.onLineBreak(Action.LineBreak(input[0]!!, 0))

        storyManager.onLineBreak(Action.LineBreak(input[0]!!, 2))
        storyManager.onLineBreak(Action.LineBreak(input[0]!!, 4))
        storyManager.undo()
        storyManager.undo()
        storyManager.undo()

        val newStory = storyManager.currentStory.value.stories

        assertEquals(
            "The size of the story can't have changed",
            currentStory.size,
            newStory.size
        )

        currentStory.values.zip(newStory.values).forEach { (storyUnit1, storyUnit2) ->
            if (storyUnit1.type != storyUnit2.type) fail()

            if (storyUnit1.type != StoryTypes.SPACE.type &&
                storyUnit1.type != StoryTypes.LARGE_SPACE.type
            ) {
                assertEquals(storyUnit1.id, storyUnit2.id)
            }
        }
    }

    @Test
    fun `spaces should be correct in the story`() {
        val storyManager = StoryTellerManager(dispatcher = Dispatchers.Main, userId = { "" })
        val input = MapStoryData.singleCheckItem()

        storyManager.run {
            initDocument(Document(content = input, userId = ""))

            onLineBreak(Action.LineBreak(input[0]!!, 0))
            onLineBreak(Action.LineBreak(input[0]!!, 2))
            onLineBreak(Action.LineBreak(input[0]!!, 4))

            undo()
            undo()
            undo()

            redo()
            redo()
        }

        val newStory = storyManager.currentStory.value.stories

        newStory.forEach { (position, storyUnit) ->
            val isEven = position % 2 == 0

            if (isEven) {
                assertNotEquals(StoryTypes.SPACE.type, storyUnit.type)
            } else {
                assertEquals(StoryTypes.SPACE.type, storyUnit.type)
            }
        }
    }

    @Test
    fun `initializing the stories 2 times should not add the last story unit twice`() = runTest {
        val storyManager = StoryTellerManager(dispatcher = Dispatchers.Main, userId = { "" })
        storyManager.initDocument(Document(content = complexMessagesRepository.history(), userId = ""))

        val stories = storyManager.currentStory.value.stories
        assertEquals(StoryTypes.LARGE_SPACE.type, stories.values.last().type)

        storyManager.initDocument(Document(content = stories, userId = ""))

        val newStories = storyManager.currentStory.value.stories
        val storyList = newStories.values.toList()

        assertEquals(StoryTypes.LARGE_SPACE.type, storyList.last().type)
        assertNotEquals(StoryTypes.LARGE_SPACE.type, storyList[storyList.lastIndex - 1].type)
    }

    @Test
    fun `it should be possible to select messages`() = runTest {
        val storyManager = StoryTellerManager(dispatcher = Dispatchers.Main, userId = { "" })
        storyManager.initDocument(Document(content = complexMessagesRepository.history(), userId = ""))

        storyManager.onSelected(true, 1)
        storyManager.onSelected(true, 3)
        storyManager.onSelected(true, 5)

        assertEquals(setOf(1, 3, 5), storyManager.onEditPositions.value)
    }

    @Test
    fun `it should be possible to delete selected messages`() = runTest {
        val storyManager = StoryTellerManager(dispatcher = Dispatchers.Main, userId = { "" })
        storyManager.initDocument(Document(content = messagesRepo.history(), userId = ""))

        val selectionCount = 3
        val selections = buildList {
            repeat(selectionCount) { index ->
                this.add(index * 2)
            }
        }

        selections.forEach { index ->
            storyManager.onSelected(true, index)
        }

        val initialStories = storyManager.currentStory.value.stories
        val initialSize = initialStories.size

        val selectedStories = selections.map { position ->
            initialStories[position]!!
        }

        storyManager.deleteSelection()

        val newStories = storyManager.currentStory.value.stories
        assertEquals(initialSize - selectionCount * 2, newStories.size)

        selectedStories.forEach { storyStep ->
            assertFalse(
                "The deleted story step should not be in the manager anymore",
                newStories.values.map { it.id }.contains(storyStep.id)
            )
        }

        assertTrue(
            "The selection should be empty now",
            storyManager.onEditPositions.value.isEmpty()
        )
    }

    @Test
    fun `it should be possible to undo bulk deletion`() = runTest {
        val storyManager = StoryTellerManager(dispatcher = Dispatchers.Main, userId = { "" })
        storyManager.initDocument(Document(content = messagesRepo.history(), userId = ""))

        val selectionCount = 3
        val selections = buildList {
            repeat(selectionCount) { index ->
                this.add(index * 2 + 1)
            }
        }

        selections.forEach { index ->
            storyManager.onSelected(true, index)
        }

        val initialStories = storyManager.currentStory.value.stories
        val initialSize = initialStories.size

        val selectedStories = selections.map { position ->
            initialStories[position]!!
        }

        storyManager.deleteSelection()

        val newStories = storyManager.currentStory.value.stories
        assertEquals(initialSize - selectionCount * 2, newStories.size)

        selectedStories.forEach { storyStep ->
            assertFalse(
                "The deleted story step should not be in the manager anymore",
                newStories.values.map { it.id }.contains(storyStep.id)
            )
        }

        assertTrue(
            "The selection should be empty now",
            storyManager.onEditPositions.value.isEmpty()
        )

        storyManager.undo()
    }

    @Test
    fun `when clicking in the last position, a message should be added at the bottom`() = runTest {
        val storyManager = StoryTellerManager(dispatcher = Dispatchers.Main, userId = { "" })
        storyManager.initDocument(Document(content = imagesInLineRepo.history(), userId = ""))

        storyManager.clickAtTheEnd()

        val currentStory = storyManager.currentStory.value.stories
        val lastContentStory = currentStory[currentStory.size - 3]

        assertEquals(lastContentStory!!.type, StoryTypes.MESSAGE.type)
        assertEquals(storyManager.currentStory.value.focusId, lastContentStory.id)
    }
}
