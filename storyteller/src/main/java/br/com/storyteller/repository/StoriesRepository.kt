package br.com.storyteller.repository

import br.com.storyteller.model.StoryUnit

interface StoriesRepository {

    suspend fun history(): List<StoryUnit>
}
