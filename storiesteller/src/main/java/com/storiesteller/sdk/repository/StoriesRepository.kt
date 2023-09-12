package com.storiesteller.sdk.repository

import com.storiesteller.sdk.models.story.StoryStep

interface StoriesRepository {

    suspend fun history(): Map<Int, StoryStep>
}
