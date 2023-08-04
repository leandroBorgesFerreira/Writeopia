package com.github.leandroborgesferreira.storytellerapp.editor.configuration.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FormatColorReset
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.leandroborgesferreira.storytellerapp.common_ui.SlideInBox

// This screen could live in a module for extra Composables
@Composable
fun BoxScope.HeaderEdition(
    modifier: Modifier = Modifier,
    availableColors: List<Int>,
    onColorSelection: (Int?) -> Unit,
    visibilityState: Boolean,
    outsideClick: () -> Unit,
) {
    val colors = listOf(Color.Transparent.toArgb()) + availableColors
    val shape = CircleShape
    val topCorner = CornerSize(16.dp)
    val bottomCorner = CornerSize(0.dp)

    SlideInBox(
        modifier = modifier.align(Alignment.BottomCenter),
        editState = visibilityState,
        outsideClick = outsideClick,
        animationLabel = "HeaderEditionAnimation",
    ) {
        Column(
            modifier = modifier
                .clip(
                    RoundedCornerShape(
                        topCorner,
                        topCorner,
                        bottomCorner,
                        bottomCorner
                    )
                )
                .background(MaterialTheme.colorScheme.surface)
                .padding(vertical = 16.dp)
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = "Header",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                itemsIndexed(colors) { i, color ->
                    Box(
                        modifier = Modifier
                            .clickable {
                                onColorSelection(if (i == 0) null else color)
                            }
                            .border(BorderStroke(1.dp, Color.LightGray), shape = shape)
                            .clip(shape)
                            .size(50.dp)
                            .background(Color(color))
                    ) {
                        if (i == 0) {
                            Icon(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(40.dp),
                                imageVector = Icons.Outlined.FormatColorReset,
                                contentDescription = "Remove color",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Preview
@Composable
fun HeaderEditionPreview() {
    val colors = listOf(
        Color.Blue.toArgb(),
        Color.White.toArgb(),
        Color.Cyan.toArgb(),
        Color.LightGray.toArgb(),
        Color.Red.toArgb(),
    )

    Box(modifier = Modifier.background(Color.White)) {
        HeaderEdition(
            availableColors = colors,
            onColorSelection = {},
            outsideClick = {},
            visibilityState = true
        )
    }
}