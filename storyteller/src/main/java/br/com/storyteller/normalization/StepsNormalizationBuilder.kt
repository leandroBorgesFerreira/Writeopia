package br.com.storyteller.normalization

import br.com.storyteller.model.StoryStep

class StepsNormalizationBuilder {

    private val normalizations: MutableList<(List<StoryStep>) -> List<StoryStep>> = mutableListOf()

    fun addNormalization(normalization: (List<StoryStep>) -> List<StoryStep>) {
        normalizations.add(normalization)
    }

    fun build(): (List<StoryStep>) -> List<StoryStep> = reduceNormalizations(normalizations)

    private fun reduceNormalizations(
        normalizations: List<(List<StoryStep>) -> List<StoryStep>>
    ): (List<StoryStep>) -> List<StoryStep> =
        normalizations.reduce { fn, gn ->
            return { fn(gn(it)) }
        }
}
