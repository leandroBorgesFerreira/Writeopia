package br.com.leandroferreira.app_sample.screens.messagelist

import br.com.leandroferreira.app_sample.data.messages
import br.com.leandroferreira.app_sample.data.messagesMap
import br.com.leandroferreira.storyteller.model.story.StoryUnit
import br.com.leandroferreira.storyteller.repository.StoriesRepository

class MessageListRepo: StoriesRepository {

    override suspend fun history(): List<StoryUnit> = messages()

    override suspend fun historyMap(): Map<Int, StoryUnit> = messagesMap()
}
