package com.github.leandroborgesferreira.storyteller.utils.alias

import com.github.leandroborgesferreira.storyteller.models.story.StoryStep

typealias DocumentContent = Map<Int, StoryStep>

typealias UnitsNormalizationMap = (Map<Int, List<StoryStep>>) -> Map<Int, StoryStep>

internal typealias UnitsMapTransformation = (Map<Int, StoryStep>) -> Map<Int, StoryStep>

