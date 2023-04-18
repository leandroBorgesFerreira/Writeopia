package br.com.storyteller.drawer.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.storyteller.drawer.StoryUnitDrawer
import br.com.storyteller.model.StoryUnit

class AddButtonDrawer : StoryUnitDrawer {

    @Composable
    override fun Step(step: StoryUnit, editable: Boolean, extraData: Map<String, Any>) {
        Box(modifier = Modifier.padding(5.dp)) {
            Button(
                onClick = { },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(contentColor = Color.Blue),
                modifier = Modifier.size(45.dp),
                contentPadding = PaddingValues(1.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "", tint = Color.White)
            }
        }
    }
}
