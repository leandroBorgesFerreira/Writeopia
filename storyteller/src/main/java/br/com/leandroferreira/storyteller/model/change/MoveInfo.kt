package br.com.leandroferreira.storyteller.model.change

import br.com.leandroferreira.storyteller.model.StoryUnit

data class MoveInfo(val storyUnit: StoryUnit, val fromPosition: Int, val toPosition: Int)