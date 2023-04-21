package br.com.leandroferreira.app_sample.data

import android.content.Context
import br.com.leandroferreira.storyteller.model.StoryUnit
import br.com.leandroferreira.storyteller.repository.StoriesRepository

class StoriesRepo(private val context: Context): StoriesRepository {

    override suspend fun history(): List<StoryUnit> = images()
//        syncHistory(context)

}
