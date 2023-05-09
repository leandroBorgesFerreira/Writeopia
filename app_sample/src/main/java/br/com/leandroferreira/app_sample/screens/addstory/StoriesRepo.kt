package br.com.leandroferreira.app_sample.screens.addstory

import android.content.Context
import br.com.leandroferreira.app_sample.data.syncHistory
import br.com.leandroferreira.app_sample.parse.toEntity
import br.com.leandroferreira.storyteller.model.story.GroupStep
import br.com.leandroferreira.storyteller.model.story.StoryStep
import br.com.leandroferreira.storyteller.model.story.StoryUnit
import br.com.leandroferreira.storyteller.persistence.dao.DocumentDao
import br.com.leandroferreira.storyteller.persistence.dao.StoryUnitDao
import br.com.leandroferreira.storyteller.persistence.entity.document.DocumentEntity
import java.util.UUID

class StoriesRepo(
    private val context: Context,
    private val documentDao: DocumentDao,
) {

    fun history(): Map<Int, StoryUnit> = syncHistory(context)

    suspend fun loadDocuments() = documentDao.loadAllDocuments()

}
