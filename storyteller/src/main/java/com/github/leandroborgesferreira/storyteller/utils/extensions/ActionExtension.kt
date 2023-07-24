package com.github.leandroborgesferreira.storyteller.utils.extensions

import com.github.leandroborgesferreira.storyteller.model.action.Action
import com.github.leandroborgesferreira.storyteller.model.action.BackstackAction

internal fun Action.toBackStack(): BackstackAction {
    when (backstackAction) {
        is BackstackAction.BulkDelete -> TODO()
        is BackstackAction.Delete -> TODO()
        is BackstackAction.Merge -> TODO()
        is BackstackAction.Move -> TODO()
        is BackstackAction.StoryStateChange -> TODO()
        is BackstackAction.StoryTextChange -> TODO()
    }

    when (this) {
        is Action.AddStory -> BackstackAction.Add(position = position, storyStep = storyStep)
//        is Action.AddText -> BackstackAction.StoryTextChange(position = position, storyStep = storyStep)
        is Action.BulkDelete -> BackstackAction.BulkDelete(deletedUnits)
        is Action.Check -> BackstackAction.StoryStateChange(storyStep, position)
        is Action.DeleteStory -> BackstackAction.Delete(storyStep, position)
        is Action.LineBreak -> BackstackAction.Add(position = position, storyStep = storyStep)
        is Action.Merge -> BackstackAction.Merge(receiver, sender, positionFrom, positionTo)
        is Action.Move -> BackstackAction.Move(storyStep, positionFrom, positionTo)
        is Action.TextEdit -> BackstackAction.StoryStateChange()
    }
}