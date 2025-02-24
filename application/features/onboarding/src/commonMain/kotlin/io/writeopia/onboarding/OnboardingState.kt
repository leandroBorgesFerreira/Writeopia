package io.writeopia.onboarding

enum class OnboardingState(val label: String) {
    CONFIGURATION("CONFIGURATION"),
    CONGRATULATION("CONGRATULATION"),
    HIDDEN("HIDDEN"),
    COMPLETE("COMPLETE");

    fun shouldShow() = this == CONFIGURATION || this == CONGRATULATION
}
