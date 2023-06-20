package com.github.leandroborgesferreira.storyteller.model.change

import com.github.leandroborgesferreira.storyteller.model.story.StoryStep

data class BulkDelete(val deletedUnits: Map<Int, StoryStep>)
