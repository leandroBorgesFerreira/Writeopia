package br.com.leandroferreira.app_sample.screens.addstory

import android.content.Context
import br.com.leandroferreira.app_sample.data.syncHistory
import com.github.leandroferreira.storyteller.model.story.StoryUnit
import com.github.leandroferreira.storyteller.persistence.dao.DocumentDao

class StoriesRepo(
    private val context: Context,
    private val documentDao: DocumentDao,
) {

    fun history(): Map<Int, StoryUnit> = syncHistory(context)

    suspend fun loadDocuments() = documentDao.loadAllDocuments()

}
