package io.storiesteller.sdk.repository

import io.storiesteller.sdk.models.story.StoryStep

interface StoriesRepository {

    suspend fun history(): Map<Int, StoryStep>
}
