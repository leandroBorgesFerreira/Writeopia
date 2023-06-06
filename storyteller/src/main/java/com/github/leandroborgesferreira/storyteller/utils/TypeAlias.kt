package com.github.leandroborgesferreira.storyteller.utils

import com.github.leandroborgesferreira.storyteller.model.story.StoryStep

typealias UnitsNormalization = (Iterable<StoryStep>) -> List<StoryStep>

typealias UnitsNormalizationMap = (Map<Int, List<StoryStep>>) -> Map<Int, StoryStep>

internal typealias UnitsMapTransformation = (Map<Int, StoryStep>) -> Map<Int, StoryStep>

