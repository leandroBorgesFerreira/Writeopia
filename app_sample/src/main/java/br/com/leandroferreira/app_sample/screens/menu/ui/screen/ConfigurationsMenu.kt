package br.com.leandroferreira.app_sample.screens.menu.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.leandroferreira.app_sample.R
import br.com.leandroferreira.app_sample.screens.menu.viewmodel.ChooseNoteViewModel
import br.com.leandroferreira.app_sample.views.SlideInBox

@Composable
internal fun BoxScope.ConfigurationsMenu(editState: Boolean, viewModel: ChooseNoteViewModel) {
    // Todo: Extract to a global use function
    SlideInBox(
        modifier = Modifier.align(Alignment.BottomCenter),
        editState = editState,
        animationLabel = "configurationsMenuAnimation"
    ) { isEdit ->
        if (isEdit) {
            val topCorner = CornerSize(16.dp)
            val bottomCorner = CornerSize(0.dp)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(
                            topCorner,
                            topCorner,
                            bottomCorner,
                            bottomCorner
                        )
                    )
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(16.dp),
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Ordering",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                ArrangementOptions(
                    listOptionClick = viewModel::listArrangementSelected,
                    gridOptionClick = viewModel::gridArrangementSelected,
                )

                Spacer(modifier = Modifier.height(90.dp))
            }
        } else {
            Box(
                modifier = Modifier
                    .height(0.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ArrangementOptions(listOptionClick: () -> Unit, gridOptionClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Image(
            modifier = Modifier
                .orderConfigModifier(clickable = gridOptionClick)
                .weight(1F),
            imageVector = Icons.Outlined.Dashboard,
            contentDescription = stringResource(R.string.staggered_card)
        )

        Image(
            modifier = Modifier
                .orderConfigModifier(clickable = listOptionClick)
                .weight(1F),
            imageVector = Icons.Outlined.List,
            contentDescription = stringResource(R.string.note_list)
        )
    }
}


private fun Modifier.orderConfigModifier(clickable: () -> Unit): Modifier =
    padding(8.dp)
        .clip(RoundedCornerShape(6.dp))
        .background(Color.Cyan)
        .clickable(onClick = clickable)
        .padding(6.dp)

@Preview
@Composable
fun ArrangementOptions_Preview() {
    ArrangementOptions({}, {})
}
