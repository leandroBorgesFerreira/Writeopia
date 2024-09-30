package io.writeopia.features.notifications

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun NotificationsScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            "No notifications (to be implemented)",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
