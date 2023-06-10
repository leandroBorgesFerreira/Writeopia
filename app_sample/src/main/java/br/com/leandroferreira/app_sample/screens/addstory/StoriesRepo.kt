package br.com.leandroferreira.app_sample.screens.addstory

import android.content.Context
import br.com.leandroferreira.app_sample.data.mockHistory
import com.github.leandroborgesferreira.storyteller.model.story.StoryStep
import com.github.leandroborgesferreira.storyteller.persistence.dao.DocumentDao

class StoriesRepo(
    private val context: Context,
    private val documentDao: DocumentDao,
) {

    fun history(): Map<Int, StoryStep> = mockHistory(context)

    suspend fun loadDocuments() = documentDao.loadAllDocuments()

}
