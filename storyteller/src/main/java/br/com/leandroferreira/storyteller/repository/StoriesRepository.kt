package br.com.leandroferreira.storyteller.repository

import br.com.leandroferreira.storyteller.model.story.StoryUnit

interface StoriesRepository {

    suspend fun history(): Map<Int, StoryUnit>
}
