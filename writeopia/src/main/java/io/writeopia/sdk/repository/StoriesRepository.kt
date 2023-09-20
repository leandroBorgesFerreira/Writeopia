package io.writeopia.sdk.repository

import io.writeopia.sdk.models.story.StoryStep

internal interface StoriesRepository {

    suspend fun history(): Map<Int, StoryStep>
}
