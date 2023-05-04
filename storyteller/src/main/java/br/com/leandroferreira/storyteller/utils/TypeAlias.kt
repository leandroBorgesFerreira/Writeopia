package br.com.leandroferreira.storyteller.utils

import br.com.leandroferreira.storyteller.model.StoryUnit

typealias UnitsNormalization = (Iterable<StoryUnit>) -> List<StoryUnit>

typealias UnitsNormalizationMap = (Map<Int, List<StoryUnit>>) -> Map<Int, StoryUnit>

internal typealias UnitsMapTransformation = (Map<Int, StoryUnit>) -> Map<Int, StoryUnit>

