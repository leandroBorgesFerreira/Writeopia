package com.github.leandroborgesferreira.storytellerapp.note_menu.ui.dto

import com.github.leandroborgesferreira.storyteller.models.story.StoryStep

internal data class DocumentUi(
    val documentId: String,
    val title: String,
    val lastEdit: String,
    val preview: List<StoryStep>,
    val selected: Boolean,
)

