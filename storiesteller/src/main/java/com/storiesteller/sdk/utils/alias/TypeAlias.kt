package com.storiesteller.sdk.utils.alias

import com.storiesteller.sdk.models.story.StoryStep

typealias DocumentContent = Map<Int, StoryStep>

typealias UnitsNormalizationMap = (Map<Int, List<StoryStep>>) -> Map<Int, StoryStep>

internal typealias UnitsMapTransformation = (Map<Int, StoryStep>) -> Map<Int, StoryStep>

