package io.writeopia.manager

import io.writeopia.sdk.model.action.Action
import io.writeopia.sdk.models.document.Document
import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sdk.repository.StoriesRepository
import io.writeopia.utils.MapStoryData
import io.writeopia.sdk.manager.WriteopiaManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class WriteopiaManagerTest {

//    @get:Rule
//    val mainDispatcherRule = MainDispatcherRule()

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
        val manager = WriteopiaManager(dispatcher = UnconfinedTestDispatcher(), userId = { "" })

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
        val storyManager = WriteopiaManager(dispatcher = UnconfinedTestDispatcher(testScheduler), userId = { "" })
        val oldSize = messagesRepo.history().size

        val now = Clock.System.now()

        storyManager.initDocument(
            Document(
                content = messagesRepo.history(),
                lastUpdatedAt = now,
                createdAt = now,
                userId = ""
            )
        )

        val newStory = storyManager.currentStory.value.stories

        assertEquals(
            oldSize * 2 + 1,
            newStory.size,
            "a space should be added between each step"
        )
    }

    @Test
    fun `when a line break happens, one (and only one) new item should be created`() {
        val input = MapStoryData.singleCheckItem()
        val checkItem = input[0]

        val now = Clock.System.now()

        val storyManager =
            WriteopiaManager(dispatcher = UnconfinedTestDispatcher(), userId = { "" }).apply {
                initDocument(Document(content = input, userId = "", createdAt = now, lastUpdatedAt = now))
            }

        val currentStory = storyManager.currentStory.value.stories

        storyManager.onLineBreak(
            Action.LineBreak(storyStep = checkItem!!, position = 0)
        )

        val newStory = storyManager.currentStory.value.stories

        assertEquals("check_item", newStory[0]!!.type.name, "the first item should be a check_item")
        assertEquals(
            "check_item",
            newStory[2]!!.type.name,
            "the second item should be a check_item"
        )
        assertEquals(
            currentStory.size + 2,
            newStory.size,
            "the size of the story should be 5"
        )
    }

    @Test
    fun `merge request should work`() = runTest {
        val storyManager = WriteopiaManager(dispatcher = UnconfinedTestDispatcher(testScheduler), userId = { "" })
        val now = Clock.System.now()
        storyManager.initDocument(
            Document(
                content = imagesInLineRepo.history(),
                userId = "",
                createdAt = now,
                lastUpdatedAt = now
            )
        )

        val currentStory = storyManager.currentStory.value.stories
        val initialSize = currentStory.size

        val positionFrom = 2
        val positionTo = 0
        val sender = currentStory[positionFrom]!!
        val receiver = currentStory[positionTo]!!

        assertFalse(currentStory[positionTo]!!.isGroup, "The first step is not a Group")

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
            initialSize - 2,
            newStory.size,
            "One image should be removed and one space"
        )
        assertEquals(
            true,
            newStory[positionTo]?.isGroup,
            "The first step should be now a GroupStep"
        )
    }

    @Test
    fun `merge request should work2`() = runTest {
        val storyManager = WriteopiaManager(dispatcher = UnconfinedTestDispatcher(testScheduler), userId = { "" })
        val now = Clock.System.now()

        storyManager.initDocument(
            Document(
                content = imagesInLineRepo.history(),
                userId = "",
                createdAt = now,
                lastUpdatedAt = now
            )
        )

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
            true,
            newStory[positionTo]?.isGroup,
            "The first image should be a GroupImage now"
        )
        assertTrue(newStory[positionFrom] is StoryStep, "Other images should still exist")
    }

    @Test
    fun `multiple merge requests should work`() = runTest {
        val storyManager = WriteopiaManager(dispatcher = UnconfinedTestDispatcher(testScheduler), userId = { "" })
        val now = Clock.System.now()
        storyManager.initDocument(
            Document(
                content = imagesInLineRepo.history(),
                userId = "",
                createdAt = now,
                lastUpdatedAt = now
            )
        )

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

        assertEquals(initialSize - 2, newHistory.size, "One space and one image were removed")
        assertEquals(
            true,
            newHistory[positionTo]?.isGroup,
            "The first message should be a GroupImage instead of a Image now"
        )
        assertEquals(
            2,
            newHistory[positionTo]!!.steps.size,
            "The new created GroupImage should have 2 images"
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
            3,
            newHistory3.size,
            "The minimum side should be 4 (group, space, large_space)"
        )
        assertEquals(true, newHistory3[positionTo]?.isGroup, "The GroupImage should still exist")
        assertEquals(
            3,
            newHistory3[positionTo]!!.steps.size,
            "Now the group has 3 images"
        )
    }

    @Test
    fun `it should be possible to merge an image inside a message group`() = runTest {
        val now = Clock.System.now()

        val storyManager = WriteopiaManager(dispatcher = UnconfinedTestDispatcher(testScheduler), userId = { "" })
        storyManager.initDocument(
            Document(
                content = imageGroupRepo.history(),
                userId = "",
                createdAt = now,
                lastUpdatedAt = now
            )
        )

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

        assertEquals(initialSize - 2, newStory.size, "One image and one space were removed")
        assertTrue(newStory[positionTo]?.isGroup == true)
        assertEquals(
            initialImageGroupSize + 1,
            newStory[positionTo]!!.steps.size,
            "One element was added to the GroupStep"
        )
    }

    @Test
    fun `it should be possible to merge an image outside a message group`() = runTest {
        val now = Clock.System.now()

        val storyManager = WriteopiaManager(dispatcher = UnconfinedTestDispatcher(testScheduler), userId = { "" })
        storyManager.initDocument(
            Document(
                content = imageGroupRepo.history(),
                userId = "",
                createdAt = now,
                lastUpdatedAt = now
            )
        )
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
            StoryTypes.GROUP_IMAGE.type,
            newStory[positionTo]!!.type,
            "The image should be now in the position 3, because of spaces."
        )
        assertEquals(
            initialGroupSize - 1,
            newStory[positionFrom]!!.steps.size,
            "The new story now it the GroupImage"
        )
    }

    @Test
    @Ignore
    fun `when moving outside of a group, the parent Id should be null now`() = runTest {
        val now = Clock.System.now()

        val storyManager = WriteopiaManager(dispatcher = UnconfinedTestDispatcher(testScheduler), userId = { "" })
        storyManager.initDocument(
            Document(
                content = imageGroupRepo.history(),
                userId = "",
                createdAt = now,
                lastUpdatedAt = now
            )
        )

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
            StoryTypes.IMAGE.type,
            lastContentStep.type,
            "The last StoryUnit should be an image."
        )
        assertEquals(
            storyToMove.id,
            lastContentStep.id,
            "The image should be in the correct place now."
        )
        assertNull(
            "The parent of the separated image, should not be there.",
            lastContentStep.parentId
        )
        assertFalse(
            newStory[positionFrom]!!.steps.any { storyUnit ->
                storyUnit.id == storyToMove.id
            },
            "The moved image should not be in the group anymore"
        )
    }

    @Test
    fun `it should be possible to switch message places`() = runTest {
        val now = Clock.System.now()

        val simpleMessagesRepo: StoriesRepository = object : StoriesRepository {
            override suspend fun history(): Map<Int, StoryStep> = MapStoryData.simpleMessages()
        }

        val storyManager = WriteopiaManager(dispatcher = UnconfinedTestDispatcher(testScheduler), userId = { "" })
        storyManager.initDocument(
            Document(
                content = simpleMessagesRepo.history(),
                userId = "",
                createdAt = now,
                lastUpdatedAt = now
            )
        )

        val positionFrom = 0
        val positionTo = 3

        val currentStory = storyManager.currentStory.value.stories
        val storyUnitToMove = currentStory[positionFrom]!!

        storyManager.moveRequest(Action.Move(storyUnitToMove, positionFrom, positionTo))

        val newStory = storyManager.currentStory.value.stories

        assertEquals(
            newStory[positionTo]!!.text,
            storyUnitToMove.text,
            "The first story should have been moved"
        )
    }

    @Test
    fun `it should be possible to revert move`() = runTest {
        val now = Clock.System.now()

        val simpleMessagesRepo: StoriesRepository = object : StoriesRepository {
            override suspend fun history(): Map<Int, StoryStep> = MapStoryData.simpleMessages()
        }

        val storyManager = WriteopiaManager(dispatcher = UnconfinedTestDispatcher(testScheduler), userId = { "" })

        storyManager.initDocument(
            Document(
                content = simpleMessagesRepo.history(),
                userId = "",
                createdAt = now,
                lastUpdatedAt = now
            )
        )

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
        val now = Clock.System.now()

        val storyManager = WriteopiaManager(dispatcher = UnconfinedTestDispatcher(testScheduler), userId = { "" })
        storyManager.initDocument(
            Document(
                content = imageGroupRepo.history(),
                userId = "",
                createdAt = now,
                lastUpdatedAt = now
            )
        )
        val groupPosition = 0

        val currentStory = storyManager.currentStory.value.stories
        assertEquals(
            StoryTypes.GROUP_IMAGE.type,
            currentStory[groupPosition]!!.type,
            "initial the story unit should be a group"
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
            StoryTypes.IMAGE.type,
            newStory[groupPosition]!!.type,
            "the group become just an image because there's only a single image"
        )
    }

    @Test
    fun `when deleting a message it should not leave consecutive spaces`() = runTest {
        val now = Clock.System.now()

        val storyManager = WriteopiaManager(dispatcher = UnconfinedTestDispatcher(testScheduler), userId = { "" })
        storyManager.initDocument(
            Document(
                content = messagesRepo.history(),
                userId = "",
                createdAt = now,
                lastUpdatedAt = now
            )
        )

        storyManager.onDelete(
            Action.DeleteStory(
                storyManager.currentStory.value.stories[3]!!, 3
            )
        )

        val stack: MutableList<StoryStep> = mutableListOf()

        storyManager.currentStory.value.stories.forEach { (_, storyUnit) ->
            if (stack.isNotEmpty() && stack.lastOrNull()?.type?.name == "space" && storyUnit.type.name == "space") {
                fail("Consecutive spaces happened.")
            }

            stack.add(storyUnit)
        }
    }

    @Test
    fun `when a line break happens, a new story unit with the same type should be created - simple`() =
        runTest {
            val now = Clock.System.now()

            val storyManager = WriteopiaManager(dispatcher = UnconfinedTestDispatcher(testScheduler), userId = { "" })
            storyManager.initDocument(
                Document(
                    content = singleMessageRepo.history(),
                    userId = "",
                    createdAt = now,
                    lastUpdatedAt = now
                )
            )

            val stories = storyManager.currentStory.value.stories
            val initialSize = stories.size
            val position = 0

            storyManager.onLineBreak(Action.LineBreak(stories[position]!!, position = position))

            assertEquals(
                initialSize + 2,
                storyManager.currentStory.value.stories.size,
                "2 new stories should have been added"
            )
        }

    @Test
    fun `when a line break happens, a new story unit with the same type should be created - complex`() =
        runTest {
            val now = Clock.System.now()

            val storyManager = WriteopiaManager(dispatcher = UnconfinedTestDispatcher(testScheduler), userId = { "" })
            storyManager.initDocument(
                Document(
                    content = messagesRepo.history(),
                    userId = "",
                    createdAt = now,
                    lastUpdatedAt = now
                )
            )

            val stories = storyManager.currentStory.value.stories
            val initialSize = stories.size
            val breakPosition = 0

            storyManager.onLineBreak(Action.LineBreak(stories[breakPosition]!!, breakPosition))

            val newStory = storyManager.currentStory.value.stories

            assertEquals(
                initialSize + 2,
                newStory.size,
                "2 new stories should have been added"
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
        val now = Clock.System.now()

        val storyManager = WriteopiaManager(dispatcher = UnconfinedTestDispatcher(testScheduler), userId = { "" })
        storyManager.initDocument(
            Document(
                content = complexMessagesRepository.history(),
                userId = "",
                createdAt = now,
                lastUpdatedAt = now
            )
        )

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

        assertEquals(2, (newStory[1]!!).steps.size, "The images should have been merged")

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
            3,
            (newStory2[1]!!).steps.distinctBy { storyUnit -> storyUnit.localId }.size,
            "The images should have been merged"
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
            2,
            (newStory3[1]!!).steps.size,
            "One image should have been separated"
        )
        assertEquals(
            storyToMove.id,
            newStory3[5]!!.id,
            "The correct StoryUnit should have been moved"
        )
    }

    @Test
    fun `it should be possible to add content and undo it - 1 unit`() {
        val now = Clock.System.now()

        val storyManager = WriteopiaManager(dispatcher = UnconfinedTestDispatcher(), userId = { "" })
        val input = MapStoryData.singleCheckItem()

        storyManager.initDocument(Document(content = input, userId = "", createdAt = now, lastUpdatedAt = now))
        val currentStory = storyManager.currentStory.value.stories

        storyManager.onLineBreak(Action.LineBreak(input[0]!!, 0))
        storyManager.undo()
        val newStory = storyManager.currentStory.value.stories

        assertEquals(currentStory.size, newStory.size)
    }

    @Test
    fun `it should be possible to add content and undo it - many units`() = runTest {
        val now = Clock.System.now()

        val storyManager = WriteopiaManager(dispatcher = UnconfinedTestDispatcher(testScheduler), userId = { "" })
        val input = MapStoryData.singleCheckItem()

        storyManager.initDocument(Document(content = input, userId = "", createdAt = now, lastUpdatedAt = now))
        val currentStory = storyManager.currentStory.value.stories

        storyManager.onLineBreak(Action.LineBreak(input[0]!!, 0))

        storyManager.onLineBreak(Action.LineBreak(input[0]!!, 2))
        storyManager.onLineBreak(Action.LineBreak(input[0]!!, 4))
        storyManager.undo()
        storyManager.undo()
        storyManager.undo()

        val newStory = storyManager.currentStory.value.stories

        assertEquals(
            currentStory.size,
            newStory.size,
            "The size of the story can't have changed"
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
        val now = Clock.System.now()

        val storyManager = WriteopiaManager(dispatcher = UnconfinedTestDispatcher(), userId = { "" })
        val input = MapStoryData.singleCheckItem()

        storyManager.run {
            initDocument(Document(content = input, userId = "", createdAt = now, lastUpdatedAt = now))

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
        val now = Clock.System.now()

        val storyManager = WriteopiaManager(dispatcher = UnconfinedTestDispatcher(testScheduler), userId = { "" })
        storyManager.initDocument(
            Document(
                content = complexMessagesRepository.history(),
                userId = "",
                createdAt = now,
                lastUpdatedAt = now
            )
        )

        val stories = storyManager.currentStory.value.stories
        assertEquals(StoryTypes.LARGE_SPACE.type, stories.values.last().type)

        storyManager.initDocument(Document(content = stories, userId = "", createdAt = now, lastUpdatedAt = now))

        val newStories = storyManager.currentStory.value.stories
        val storyList = newStories.values.toList()

        assertEquals(StoryTypes.LARGE_SPACE.type, storyList.last().type)
        assertNotEquals(StoryTypes.LARGE_SPACE.type, storyList[storyList.lastIndex - 1].type)
    }

    @Test
    fun `it should be possible to select messages`() = runTest {
        val now = Clock.System.now()

        val storyManager = WriteopiaManager(dispatcher = UnconfinedTestDispatcher(testScheduler), userId = { "" })
        storyManager.initDocument(
            Document(
                content = complexMessagesRepository.history(),
                userId = "",
                createdAt = now,
                lastUpdatedAt = now
            )
        )

        storyManager.onSelected(true, 1)
        storyManager.onSelected(true, 3)
        storyManager.onSelected(true, 5)

        assertEquals(setOf(1, 3, 5), storyManager.onEditPositions.value)
    }

    @Test
    fun `it should be possible to delete selected messages`() = runTest {
        val now = Clock.System.now()

        val storyManager = WriteopiaManager(dispatcher = UnconfinedTestDispatcher(testScheduler), userId = { "" })
        storyManager.initDocument(
            Document(
                content = messagesRepo.history(),
                userId = "",
                createdAt = now,
                lastUpdatedAt = now
            )
        )

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
                newStories.values.map { it.id }.contains(storyStep.id),
                "The deleted story step should not be in the manager anymore"
            )
        }

        assertTrue(
            storyManager.onEditPositions.value.isEmpty(),
            "The selection should be empty now"
        )
    }

    @Test
    fun `it should be possible to undo bulk deletion`() = runTest {
        val now = Clock.System.now()

        val storyManager = WriteopiaManager(dispatcher = UnconfinedTestDispatcher(testScheduler), userId = { "" })
        storyManager.initDocument(
            Document(
                content = messagesRepo.history(),
                userId = "",
                createdAt = now,
                lastUpdatedAt = now
            )
        )

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
                newStories.values.map { it.id }.contains(storyStep.id),
                "The deleted story step should not be in the manager anymore"
            )
        }

        assertTrue(
            storyManager.onEditPositions.value.isEmpty(),
            "The selection should be empty now"
        )

        storyManager.undo()
    }

    @Test
    fun `when clicking in the last position, a message should be added at the bottom`() = runTest {
        val now = Clock.System.now()

        val storyManager = WriteopiaManager(dispatcher = UnconfinedTestDispatcher(testScheduler), userId = { "" })
        storyManager.initDocument(
            Document(
                content = imagesInLineRepo.history(),
                userId = "",
                createdAt = now,
                lastUpdatedAt = now
            )
        )

        storyManager.clickAtTheEnd()

        val currentStory = storyManager.currentStory.value.stories
        val lastContentStory = currentStory[currentStory.size - 3]

        assertEquals(lastContentStory!!.type, StoryTypes.MESSAGE.type)
        assertEquals(storyManager.currentStory.value.focusId, lastContentStory.id)
    }
}
