package com.github.leandroferreira.storyteller.repository

import com.github.leandroferreira.storyteller.model.story.StoryUnit

interface StoriesRepository {

    suspend fun history(): Map<Int, StoryUnit>
}
