package com.github.leandroborgesferreira.storyteller.model.story

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

data class Decoration(val backgroundColor: Color? = null) {

    constructor(backgroundColorString: String) : this(Color(backgroundColorString.toColorInt()))
}