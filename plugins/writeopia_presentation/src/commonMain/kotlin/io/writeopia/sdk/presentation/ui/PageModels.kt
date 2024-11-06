package io.writeopia.sdk.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import io.writeopia.sdk.presentation.model.SlidePage

object PageModels {

    @Composable
    fun titleAndText(slide: SlidePage, props: PresentationProps) {
        Box(Modifier.fillMaxSize()) {
            textColumn(slide, props, modifier = Modifier.align(Alignment.Center))
        }
    }

    @Composable
    fun twoColors(slide: SlidePage, props: PresentationProps) {
        Box(Modifier.fillMaxSize()) {
            Row(modifier = Modifier.align(Alignment.Center)) {
                textColumn(slide, props, modifier = Modifier.weight(1F).fillMaxHeight())

                slide.imagePath?.let { path ->
                    showImage(path, props, modifier = Modifier.fillMaxHeight())
                }
            }
        }
    }

    @Composable
    private fun textColumn(
        slide: SlidePage,
        props: PresentationProps,
        modifier: Modifier = Modifier
    ) {
        Box(modifier = modifier.background(props.colorPrimary)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text(
                    slide.title,
                    fontSize = props.titleFontSize,
                    color = props.titleTextColor,
                )

                slide.content.forEach {
                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        it.text ?: "",
                        fontSize = props.itemsFontSize,
                        color = props.itemsTextColor
                    )
                }
            }
        }
    }

    @Composable
    private fun showImage(
        imagePath: String,
        props: PresentationProps,
        modifier: Modifier = Modifier
    ) {
        Box(modifier = modifier.background(props.colorSecondary)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.align(Alignment.Center)
            ) {
                AsyncImage(
                    model = imagePath,
                    contentDescription = null,
                    modifier = Modifier.fillMaxHeight(),
                    onError = {
                        println("Error: ${it.result.throwable.message}")
                    }
                )
            }
        }
    }
}
