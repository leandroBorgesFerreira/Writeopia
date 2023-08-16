package com.github.leandroborgesferreira.storytellerapp.utils_module

import com.github.leandroborgesferreira.storyteller.model.story.Decoration
import com.github.leandroborgesferreira.storyteller.model.story.StoryStep
import com.github.leandroborgesferreira.storyteller.model.story.StoryType
import com.github.leandroborgesferreira.storyteller.network.data.DecorationApi
import com.github.leandroborgesferreira.storyteller.network.data.StoryStepApi
import com.github.leandroborgesferreira.storyteller.network.data.StoryTypeApi

fun StoryStep.toApi(): StoryStepApi =
    StoryStepApi(
        id = this.id,
        type = this.type.toApi(),
        parentId = this.parentId,
        url = this.url,
        path = this.path,
        text = this.text,
        title = this.title,
        checked = this.checked,
        steps = this.steps.map { storyStep -> storyStep.toApi() },
        decoration = this.decoration.toApi(),
    )

fun StoryType.toApi(): StoryTypeApi =
    StoryTypeApi(
        name = this.name,
        number = this.number,
    )

fun Decoration.toApi(): DecorationApi = DecorationApi(this.backgroundColor)