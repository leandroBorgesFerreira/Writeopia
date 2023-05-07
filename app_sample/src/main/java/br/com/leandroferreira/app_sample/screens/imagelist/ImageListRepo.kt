package br.com.leandroferreira.app_sample.screens.imagelist

import br.com.leandroferreira.app_sample.data.images
import br.com.leandroferreira.app_sample.data.imagesMap
import br.com.leandroferreira.storyteller.model.story.StoryUnit
import br.com.leandroferreira.storyteller.repository.StoriesRepository

class ImageListRepo: StoriesRepository {

    override suspend fun history(): Map<Int, StoryUnit> = imagesMap()
}
