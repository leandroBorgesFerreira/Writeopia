package com.github.leandroborgesferreira.storyteller.persistence.utils

import com.github.leandroborgesferreira.storyteller.model.story.StoryStep
import com.github.leandroborgesferreira.storyteller.model.story.StoryUnit

fun simpleImage(): Map<Int, StoryUnit> = mapOf(
    0 to StoryStep(
        localId = "1",
        type = "image",
    )
)

fun imageStepsList(): Map<Int, StoryUnit> = mapOf(
    0 to StoryStep(
        localId = "1",
        type = "image",
    ),
    1 to StoryStep(
        localId = "2",
        type = "image",
    ),
    2 to StoryStep(
        localId = "3",
        type = "image",
    ),
)

fun imageGroup() =
    mapOf(
        0 to StoryStep(
            localId = "1",
            type = "group_image",
            steps = listOf(
                StoryStep(
                    localId = "2",
                    type = "image",
                ),
                StoryStep(
                    localId = "3",
                    type = "image",
                ),
                StoryStep(
                    localId = "4",
                    type = "image",
                )
            )
        ),
    )
