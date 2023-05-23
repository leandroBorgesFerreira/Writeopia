package com.github.leandroferreira.storyteller.utils

import com.github.leandroferreira.storyteller.model.story.StoryUnit

typealias UnitsNormalization = (Iterable<StoryUnit>) -> List<StoryUnit>

typealias UnitsNormalizationMap = (Map<Int, List<StoryUnit>>) -> Map<Int, StoryUnit>

internal typealias UnitsMapTransformation = (Map<Int, StoryUnit>) -> Map<Int, StoryUnit>

