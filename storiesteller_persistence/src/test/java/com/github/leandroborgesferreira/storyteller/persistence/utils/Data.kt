package com.storiesteller.sdk.persistence.utils

import com.storiesteller.sdk.models.story.StoryStep
import com.storiesteller.sdk.model.story.StoryTypes

fun imageGroup() =
    mapOf(
        0 to StoryStep(
            localId = "1",
            type = StoryTypes.GROUP_IMAGE.type,
            steps = listOf(
                StoryStep(
                    localId = "2",
                    type = StoryTypes.IMAGE.type,
                ),
                StoryStep(
                    localId = "3",
                    type = StoryTypes.IMAGE.type,
                ),
                StoryStep(
                    localId = "4",
                    type = StoryTypes.IMAGE.type,
                )
            )
        ),
    )
