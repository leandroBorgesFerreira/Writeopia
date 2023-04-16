package br.com.storyteller.normalization

import br.com.storyteller.model.StoryUnit

class StepsNormalizationBuilder {

    companion object {
        fun reduceNormalizations(
            buildFunc: StepsNormalizationBuilder.() -> Unit
        ): (List<StoryUnit>) -> List<StoryUnit> =
            StepsNormalizationBuilder().apply(buildFunc).build()
    }

    private val normalizations: MutableList<(List<StoryUnit>) -> List<StoryUnit>> = mutableListOf()

    fun addNormalization(
        normalization: (List<StoryUnit>) -> List<StoryUnit>
    ): StepsNormalizationBuilder = apply {
        normalizations.add(normalization)
    }

    private fun build(): (List<StoryUnit>) -> List<StoryUnit> = reduceNormalizations(normalizations)

    private fun reduceNormalizations(
        normalizations: List<(List<StoryUnit>) -> List<StoryUnit>>
    ): (List<StoryUnit>) -> List<StoryUnit> =
        normalizations.reduce { fn, gn ->
            return { gn(fn(it)) }
        }
}
