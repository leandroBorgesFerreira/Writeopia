package br.com.leandroferreira.storyteller.model.change

import br.com.leandroferreira.storyteller.model.story.StoryUnit

data class CheckInfo(val storyUnit: StoryUnit, val position: Int, val checked: Boolean)
