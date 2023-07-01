package br.com.leandroferreira.app_sample.screens.menu.ui.screen

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.leandroferreira.app_sample.R
import br.com.leandroferreira.app_sample.views.SlideInBox

private const val INNER_PADDING = 3

@Composable
internal fun BoxScope.ConfigurationsMenu(
    editState: Boolean,
    listOptionClick: () -> Unit,
    gridOptionClick: () -> Unit
) {
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
                ArrangementSection(listOptionClick, gridOptionClick)

                SortingSection()

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
private fun SectionText(text: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 6.dp),
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onPrimary
    )
}

@Composable
private fun ArrangementOptions(listOptionClick: () -> Unit, gridOptionClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(INNER_PADDING.dp)
    ) {
        Icon(
            modifier = Modifier
                .orderConfigModifier(clickable = gridOptionClick)
                .weight(1F),
            imageVector = Icons.Outlined.Dashboard,
            contentDescription = stringResource(R.string.staggered_card),
            tint = MaterialTheme.colorScheme.onPrimary
        )

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            modifier = Modifier
                .orderConfigModifier(clickable = listOptionClick)
                .weight(1F),
            imageVector = Icons.Outlined.List,
            contentDescription = stringResource(R.string.note_list),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun ArrangementSection(listOptionClick: () -> Unit, gridOptionClick: () -> Unit) {
    SectionText(text = stringResource(R.string.arrangement))

    ArrangementOptions(
        listOptionClick = listOptionClick,
        gridOptionClick = gridOptionClick,
    )
}

@Composable
private fun SortingSection() {
    SectionText(text = stringResource(R.string.sorting))
    val optionStyle = MaterialTheme.typography.bodyLarge.copy(
        color = MaterialTheme.colorScheme.onPrimary,
        fontWeight = FontWeight.Bold
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = INNER_PADDING.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.secondary)
    ) {
        Text(
            modifier = Modifier
                .clickable { }
                .sortingOptionModifier(),
            text = stringResource(R.string.last_updated),
            style = optionStyle,
        )

        Divider(color = MaterialTheme.colorScheme.primary)

        Text(
            modifier = Modifier
                .clickable { }
                .sortingOptionModifier(),
            text = stringResource(R.string.last_edited),
            style = optionStyle,
        )

        Divider(color = MaterialTheme.colorScheme.primary)

        Text(
            modifier = Modifier
                .clickable { }
                .sortingOptionModifier(),
            text = stringResource(R.string.name),
            style = optionStyle,
        )
    }
}

private fun Modifier.sortingOptionModifier(): Modifier = fillMaxWidth().padding(12.dp)

private fun Modifier.orderConfigModifier(clickable: () -> Unit): Modifier =
    composed {
        clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.secondary)
            .clickable(onClick = clickable)
            .padding(6.dp)
    }

@Preview
@Composable
private fun ConfigurationsMenu_Preview() {
    Box(modifier = Modifier.fillMaxWidth()) {
        ConfigurationsMenu(true, {}, {})
    }
}

@Preview
@Composable
private fun ArrangementOptions_Preview() {
    ArrangementOptions({}, {})
}
