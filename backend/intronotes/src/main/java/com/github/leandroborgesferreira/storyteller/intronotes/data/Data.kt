package com.github.leandroborgesferreira.storyteller.intronotes.data

import com.github.leandroborgesferreira.storyteller.intronotes.model.StoryTypes
import com.github.leandroborgesferreira.storyteller.serialization.data.StoryStepApi
import com.github.leandroborgesferreira.storyteller.serialization.extensions.toApi
import java.util.UUID

fun supermarketList(): List<StoryStepApi> = listOf(
    StoryStepApi(
        id = UUID.randomUUID().toString(),
        type = StoryTypes.TITLE.type.toApi(),
        text = "Supermarket List",
        position = 0,
    ),
)
