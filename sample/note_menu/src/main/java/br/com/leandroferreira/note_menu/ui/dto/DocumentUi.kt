package br.com.leandroferreira.note_menu.ui.dto

import com.github.leandroborgesferreira.storyteller.model.story.StoryStep

data class DocumentUi(
    val documentId: String,
    val title: String,
    val lastEdit: String,
    val preview: List<StoryStep>,
    val selected: Boolean,
)

