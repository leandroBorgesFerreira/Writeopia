package br.com.leandroferreira.storyteller.utils

import br.com.leandroferreira.storyteller.model.StoryUnit

typealias UnitsNormalization = (Iterable<StoryUnit>) -> List<StoryUnit>

typealias UnitsNormalizationMap = (Map<Int, StoryUnit>) -> Map<Int, StoryUnit>

