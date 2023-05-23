package com.github.leandroferreira.storyteller.text.edition

import com.github.leandroferreira.storyteller.model.story.StoryStep

class TextCommandHandler(private val commandsMap: Map<String, (StoryStep, Int) -> Unit>) {

    fun handleCommand(text: String, step: StoryStep, position: Int): Boolean {
        //Todo(Leandro): Using a reverse index would improve the speed a lot.
        val commandKey: String = commandsMap.keys
            .firstOrNull(text::contains)
            ?: return false

        commandsMap[commandKey]!!.invoke(step.copy(text = text), position)

        return true
    }
}
