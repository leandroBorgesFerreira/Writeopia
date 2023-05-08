package br.com.leandroferreira.storyteller.model.change

import br.com.leandroferreira.storyteller.model.story.StoryStep

data class LineBreakInfo(val storyStep: StoryStep, val position: Int)
