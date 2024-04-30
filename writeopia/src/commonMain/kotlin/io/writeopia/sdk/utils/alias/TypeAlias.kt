package io.writeopia.sdk.utils.alias

import io.writeopia.sdk.models.story.StoryStep

typealias DocumentContent = Map<Int, StoryStep>

typealias UnitsNormalizationMap = (Map<Int, List<StoryStep>>) -> Map<Int, StoryStep>

internal typealias UnitsMapTransformation = (Map<Int, StoryStep>) -> Map<Int, StoryStep>
