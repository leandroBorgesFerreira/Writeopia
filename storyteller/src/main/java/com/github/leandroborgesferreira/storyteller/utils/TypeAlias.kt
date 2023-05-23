package com.github.leandroborgesferreira.storyteller.utils

import com.github.leandroborgesferreira.storyteller.model.story.StoryUnit

typealias UnitsNormalization = (Iterable<StoryUnit>) -> List<StoryUnit>

typealias UnitsNormalizationMap = (Map<Int, List<StoryUnit>>) -> Map<Int, StoryUnit>

internal typealias UnitsMapTransformation = (Map<Int, StoryUnit>) -> Map<Int, StoryUnit>

