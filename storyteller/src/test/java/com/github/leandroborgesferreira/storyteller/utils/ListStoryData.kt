package com.github.leandroborgesferreira.storyteller.utils

import com.github.leandroborgesferreira.storyteller.model.story.StoryStep

object ListStoryData {

    fun imageGroup(): List<StoryStep> = buildList {
        add(
            StoryStep(
                localId = "0",
                type = "group_image",
                steps = listOf(
                    StoryStep(
                        localId = "1",
                        type = "image",
                        parentId = "0"
                    ),

                    StoryStep(
                        localId = "2",
                        type = "image",
                        parentId = "0"
                    ),

                    StoryStep(
                        localId = "3",
                        type = "image",
                        parentId = "0"
                    )
                )
            )
        )
        add(
            StoryStep(
                localId = "4",
                type = "image",
            )
        )
    }

    fun spaces(): List<StoryStep> = buildList {
        add(
            StoryStep(
                localId = "1",
                type = "space",
            )
        )
        add(
            StoryStep(
                localId = "2",
                type = "space",
            )
        )
        add(
            StoryStep(
                localId = "2",
                type = "space",
            )
        )
    }

    fun spacedImageStepsList(): List<StoryStep> = buildList {
        add(
            StoryStep(
                localId = "1",
                type = "image",
            )
        )
        add(
            StoryStep(
                localId = "pamsdplams",
                type = "space",
            )
        )
        add(
            StoryStep(
                localId = "3",
                type = "image",
            )
        )
        add(
            StoryStep(
                localId = "askndpalsd",
                type = "space",
            )
        )
        add(
            StoryStep(
                localId = "5",
                type = "image",
            )
        )
    }
}
