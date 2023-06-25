package com.github.leandroborgesferreira.storyteller.utils.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
internal fun transparentTextInputColors() =
    TextFieldDefaults.colors(
        focusedIndicatorColor = Color.Transparent,
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        cursorColor = MaterialTheme.colorScheme.primary
    )