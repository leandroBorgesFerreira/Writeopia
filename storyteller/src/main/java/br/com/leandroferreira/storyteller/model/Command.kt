package br.com.leandroferreira.storyteller.model

/**
 * A command performed in a [StepUnit] is can be delete, move up, move down... anything.
 */
class Command(val type: String, val step: StoryUnit)
