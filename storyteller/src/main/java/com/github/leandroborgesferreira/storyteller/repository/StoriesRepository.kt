package com.github.leandroborgesferreira.storyteller.repository

import com.github.leandroborgesferreira.storyteller.model.story.StoryUnit

interface StoriesRepository {

    suspend fun history(): Map<Int, StoryUnit>
}
