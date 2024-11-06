package io.writeopia.editor.features.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.writeopia.common.utils.icons.WrIcons
import io.writeopia.editor.features.presentation.viewmodel.PresentationViewModel
import io.writeopia.sdk.presentation.ui.WriteopiaPresentationScreen

@Composable
fun PresentationScreen(presentationViewModel: PresentationViewModel) {
    val slides by presentationViewModel.slidesState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        if (slides.isEmpty()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            WriteopiaPresentationScreen(
                modifier = Modifier.fillMaxSize().background(Color(0xFF252525)),
                currentPage = presentationViewModel.currentPage,
                data = slides
            )
        }

        Row(modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 40.dp)) {
            val tint = MaterialTheme.colorScheme.onBackground
            val iconSize = 50.dp

            Icon(
                imageVector = WrIcons.circularArrowLeft,
                contentDescription = "Previous slide",
                modifier = Modifier.clickable(onClick = presentationViewModel::previousPage)
                    .padding(10.dp)
                    .size(iconSize),
                tint = tint
            )

            Spacer(modifier = Modifier.width(10.dp))

            Icon(
                imageVector = WrIcons.circularArrowRight,
                contentDescription = "Previous slide",
                modifier = Modifier.clickable(onClick = presentationViewModel::nextPage)
                    .padding(10.dp)
                    .size(iconSize),
                tint = tint
            )
        }
    }
}
