package io.writeopia.note_menu.ui.dto

import io.writeopia.sdk.models.story.StoryStep

data class DocumentUi(
    val documentId: String,
    val title: String,
    val lastEdit: String,
    val preview: List<StoryStep>,
    val selected: Boolean,
    val isFavorite: Boolean
)

