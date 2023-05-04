package br.com.leandroferreira.storyteller.model.change

import br.com.leandroferreira.storyteller.model.StoryUnit

data class MergeInfo(val receiver: StoryUnit, val sender: StoryUnit)
