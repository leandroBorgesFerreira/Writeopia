package io.writeopia.ui.modifiers

import io.writeopia.sdk.models.story.StoryStep
import io.writeopia.sdk.models.story.StoryTypes
import io.writeopia.sdk.models.story.Tag
import io.writeopia.ui.model.DrawStory

object SpacesModifier {

    fun modify(stories: List<DrawStory>, dragPosition: Int): List<DrawStory> {
        val space = StoryStep(type = StoryTypes.SPACE.type)
        val onDragSpace = StoryStep(type = StoryTypes.ON_DRAG_SPACE.type)
        val lastSpace = StoryStep(type = StoryTypes.LAST_SPACE.type)

        val parsed = stories.foldIndexed(emptyList<DrawStory>()) { index, acc, drawStory ->
            val lastStep = acc.lastOrNull { draw ->
                draw.storyStep.type != StoryTypes.SPACE.type &&
                    draw.storyStep.type != StoryTypes.ON_DRAG_SPACE.type
            }?.storyStep

            val lastTags = lastStep?.tags ?: emptySet()
            val currentTags = drawStory.storyStep.tags
            val newTags = mergeTags(lastTags, currentTags)

            val spaceStory =
                if (index - 1 == dragPosition) onDragSpace else space

            val spaceDraw = DrawStory(
                storyStep = spaceStory.copy(tags = newTags),
                position = index
            )

            acc + spaceDraw + drawStory
        }

        val lastIndex = parsed.lastIndex
        val fullStory = parsed + DrawStory(storyStep = lastSpace, position = lastIndex)

        return addPositionToTags(fullStory)
    }

    private fun mergeTags(tags1: Set<Tag>, tags2: Set<Tag>): Set<Tag> {
        val enums1 = tags1.filter { it.hasPosition() }.toSet()
        val enums2 = tags2.filter { it.hasPosition() }.toSet()

        return enums1.intersect(enums2)
    }

    private fun addPositionToTags(stories: List<DrawStory>): List<DrawStory> {
        val setTagPosition : (Iterable<Tag>, Int) -> Unit = { tags, position ->
            tags.filter { it.hasPosition() }.forEach { tag ->
                if (tag.hasPosition()) {
                    tag.position = position
                }
            }
        }

        val hasPositionTagFn: (DrawStory) -> Boolean = { draw ->
            draw.storyStep
                .tags
                .any { it.hasPosition() }
        }


        for (i in 0..stories.lastIndex) {
            val draw = stories[i]

            val tags = draw.storyStep.tags
            val hasPositionTag = tags.any { it.hasPosition() }

            val previousTags = if (i > 0) hasPositionTagFn(stories[i - 1]) else false
            val nextTags = if (i < stories.lastIndex) hasPositionTagFn(stories[i + 1]) else false

            if (hasPositionTag) {
                when {
                    i == 0 -> { setTagPosition(tags, -1) }

                    i == stories.lastIndex -> { setTagPosition(tags, 1) }

                    previousTags && !nextTags -> { setTagPosition(tags, -1) }

                    previousTags && nextTags -> { setTagPosition(tags, 0) }

                    !previousTags && nextTags -> { setTagPosition(tags, 1) }

                    else -> { setTagPosition(tags, 0) }
                }
            }
        }

        return stories
    }
}

