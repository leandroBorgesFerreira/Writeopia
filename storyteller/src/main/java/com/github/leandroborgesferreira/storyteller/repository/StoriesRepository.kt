package com.github.leandroborgesferreira.storyteller.repository

import com.github.leandroborgesferreira.storyteller.model.story.StoryStep

interface StoriesRepository {

    suspend fun history(): Map<Int, StoryStep>
}
