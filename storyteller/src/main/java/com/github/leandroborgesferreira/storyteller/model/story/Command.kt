package com.github.leandroborgesferreira.storyteller.model.story

/**
 * A command performed in a [StepUnit] is can be delete, move up, move down... anything.
 */
class Command(val type: String, val step: StoryUnit)
