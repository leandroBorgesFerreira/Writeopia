package com.github.leandroborgesferreira.storyteller.intronotes.data

import com.github.leandroborgesferreira.storyteller.intronotes.model.StoryTypes
import com.github.leandroborgesferreira.storyteller.serialization.data.DocumentApi
import com.github.leandroborgesferreira.storyteller.serialization.data.StoryStepApi
import com.github.leandroborgesferreira.storyteller.serialization.extensions.toApi
import java.util.UUID

fun introNotes(): List<DocumentApi> = listOf(
    connectedIntroNote()
)

private fun connectedIntroNote(): DocumentApi =
    DocumentApi(
        title = "You are connected",
        content = listOf(
            StoryStepApi(
                id = UUID.randomUUID().toString(),
                type = StoryTypes.TITLE.type.toApi(),
                position = 0,
                text = "You successfully registered in the app, enjoy the app with your newly created account"
            )
        )
    )
