package br.com.leandroferreira.app_sample.screens.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import br.com.leandroferreira.app_sample.navigation.Destinations

@Composable
fun Menu(navigateTo: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
                .background(Color.Blue)
                .clickable {
                    navigateTo(Destinations.ADD_HISTORY.id)
                }
        ) {
            Text(
                text = "Story sample",
                modifier = Modifier.align(alignment = Alignment.Center),
                style = TextStyle(fontSize = 30.sp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
                .background(Color.Cyan)
                .clickable {
                    navigateTo(Destinations.IMAGE_LIST.id)
                }
        ) {
            Text(
                text = "Simple images",
                modifier = Modifier.align(alignment = Alignment.Center),
                style = TextStyle(fontSize = 30.sp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
                .background(Color.Green)
                .clickable {
                    navigateTo(Destinations.MESSAGE_LIST.id)
                }
        ) {
            Text(
                text = "Simple messages",
                modifier = Modifier.align(alignment = Alignment.Center),
                style = TextStyle(fontSize = 30.sp)
            )
        }
    }
}
