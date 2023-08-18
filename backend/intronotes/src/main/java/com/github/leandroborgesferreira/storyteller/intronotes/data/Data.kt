package com.github.leandroborgesferreira.storyteller.intronotes.data

import com.github.leandroborgesferreira.storyteller.intronotes.model.StoryStep
import com.github.leandroborgesferreira.storyteller.intronotes.model.StoryTypes
import java.util.UUID

fun supermarketList(): List<StoryStep> = listOf(
    StoryStep(
        id = UUID.randomUUID().toString(),
        type = StoryTypes.TITLE.type,
        text = "Supermarket List",
        position = 0
    ),
)