package br.com.leandroferreira.storyteller.repository

import br.com.leandroferreira.storyteller.model.story.StoryUnit

interface StoriesRepository {

    suspend fun history(): List<StoryUnit>

    suspend fun historyMap(): Map<Int, StoryUnit>
}
