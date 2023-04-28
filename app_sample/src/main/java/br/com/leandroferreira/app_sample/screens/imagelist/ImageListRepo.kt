package br.com.leandroferreira.app_sample.screens.imagelist

import br.com.leandroferreira.app_sample.data.images
import br.com.leandroferreira.storyteller.model.StoryUnit
import br.com.leandroferreira.storyteller.repository.StoriesRepository

class ImageListRepo: StoriesRepository {

    override suspend fun history(): List<StoryUnit> = images()

}
