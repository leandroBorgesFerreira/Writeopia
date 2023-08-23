package com.github.leandroborgesferreira.storyteller.repository

import com.github.leandroborgesferreira.storyteller.models.story.StoryStep

interface StoriesRepository {

    suspend fun history(): Map<Int, StoryStep>
}
