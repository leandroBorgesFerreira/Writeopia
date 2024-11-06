package io.writeopia.sdk.presentation.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import io.writeopia.sdk.presentation.model.SlidePage
import kotlinx.coroutines.flow.StateFlow

@Composable
fun WriteopiaPresentationScreen(
    modifier: Modifier = Modifier,
    currentPage: StateFlow<Int>,
    fontsConfiguration: PresentationProps = PresentationProps(),
    data: List<SlidePage> = Fixture.document().second,
    pageDraw: @Composable (SlidePage, PresentationProps) -> Unit = { slide, props ->
        PageModels.twoColors(slide, props)
    }
) {
    Box(modifier = modifier) {
        val page by currentPage.collectAsState()

        Crossfade(
            page,
            modifier = Modifier.align(Alignment.Center)
        ) { fadePage ->
            val slide = data[fadePage]
            pageDraw(slide, fontsConfiguration)
        }
    }
}

data class PresentationProps(
    val titleFontSize: TextUnit = 180.sp,
    val itemsFontSize: TextUnit = 36.sp,
    val titleTextColor: Color = Color.White,
    val itemsTextColor: Color = Color.White,
    val colorPrimary: Color = Color.Black,
    val colorSecondary: Color = Color.White
)

