package com.github.leandroborgesferreira.storyteller.model.story

/**
 * Class meant to be draw in the screen. It contains both the information of a story step and
 * meta information and the state of the TextEditor like if the message is selected
 */
data class DrawStory(val storyStep: StoryStep, val isSelected: Boolean) {

    val key = storyStep.key
}