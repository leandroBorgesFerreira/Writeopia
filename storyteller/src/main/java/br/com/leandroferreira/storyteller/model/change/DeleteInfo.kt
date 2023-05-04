package br.com.leandroferreira.storyteller.model.change

import br.com.leandroferreira.storyteller.model.StoryUnit

data class DeleteInfo(val storyUnit: StoryUnit, val position: Int)
