package io.writeopia.account.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.writeopia.common.utils.icons.WrIcons
import io.writeopia.model.ColorThemeOption
import kotlinx.coroutines.flow.StateFlow

@Composable
fun SettingsDialog(
    selectedThemePosition: StateFlow<Int>,
    onDismissRequest: () -> Unit,
    selectColorTheme: (ColorThemeOption) -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.7F)
                .padding(horizontal = 40.dp, vertical = 20.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                SettingsScreen(selectedThemePosition, selectColorTheme)
            }
        }
    }
}

@Composable
fun ColumnScope.SettingsScreen(
    selectedThemePosition: StateFlow<Int>,
    selectColorTheme: (ColorThemeOption) -> Unit,
) {
    val titleStyle = MaterialTheme.typography.titleLarge
    val titleColor = MaterialTheme.colorScheme.onBackground

    Text("Color Theme", style = titleStyle, color = titleColor)

    Spacer(modifier = Modifier.height(20.dp))

    ColorThemeOptions(
        selectedThemePosition = selectedThemePosition,
        selectColorTheme = selectColorTheme
    )

    Spacer(modifier = Modifier.weight(1F))

    Text("Release version: 2024-06-14", style = MaterialTheme.typography.bodySmall)
}

@Composable
private fun ColorThemeOptions(
    selectedThemePosition: StateFlow<Int>,
    selectColorTheme: (ColorThemeOption) -> Unit
) {
    val spaceWidth = 10.dp

    Row(modifier = Modifier.fillMaxWidth().height(90.dp)) {
        Option(
            text = "Light",
            imageVector = WrIcons.colorModeLight,
            contextDescription = "light",
            selectColorTheme = {
                selectColorTheme(ColorThemeOption.LIGHT)
            }
        )

        Spacer(modifier = Modifier.width(spaceWidth))

        Option(
            text = "Dark",
            imageVector = WrIcons.colorModeDark,
            contextDescription = "dark",
            selectColorTheme = {
                selectColorTheme(ColorThemeOption.DARK)
            }
        )

        Spacer(modifier = Modifier.width(spaceWidth))

        Option(
            text = "System",
            imageVector = WrIcons.colorModeSystem,
            contextDescription = "system",
            selectColorTheme = {
                selectColorTheme(ColorThemeOption.SYSTEM)
            }
        )
    }

//    HorizontalOptions(
//        modifier = Modifier,
//        selectedState = selectedThemePosition,
//        options = listOf<Pair<() -> Unit, @Composable RowScope.() -> Unit>>(
//            {
//                println("selecting light color theme!!")
//                selectColorTheme(ColorThemeOption.LIGHT)
//            } to {
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    modifier = Modifier.weight(1F)
//                ) {
//                    Icon(
//                        modifier = Modifier.weight(1F),
//                        imageVector = Icons.Outlined.LightMode,
//                        contentDescription = "staggered card",
//                        //            stringResource(R.string.staggered_card),
//                        tint = MaterialTheme.colorScheme.onPrimary
//                    )
//
//                    Text("Light", style = typography, color = color)
//                }
//            },
//            { selectColorTheme(ColorThemeOption.DARK) } to {
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    modifier = Modifier
//                        .orderConfigModifierHorizontal {}
//                        .weight(1F)
//                ) {
//                    Icon(
//                        modifier = Modifier.weight(1F),
//                        imageVector = Icons.Outlined.DarkMode,
//                        contentDescription = "staggered card",
//                        //            stringResource(R.string.staggered_card),
//                        tint = MaterialTheme.colorScheme.onPrimary
//                    )
//
//                    Text("Dark", style = typography, color = color)
//                }
//            },
//            { selectColorTheme(ColorThemeOption.SYSTEM) } to {
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    modifier = Modifier
//                        .orderConfigModifierHorizontal {}
//                        .weight(1F)
//                ) {
//                    Icon(
//                        modifier = Modifier.weight(1F),
//                        imageVector = Icons.Outlined.SystemUpdate,
//                        contentDescription = "note list",
//                        //            stringResource(R.string.note_list),
//                        tint = MaterialTheme.colorScheme.onPrimary
//                    )
//
//                    Text("System", style = typography, color = color)
//                }
//            }
//        ),
//        height = 90.dp
//    )
}

@Composable
private fun RowScope.Option(
    text: String,
    imageVector: ImageVector,
    contextDescription: String,
    selectColorTheme: (ColorThemeOption) -> Unit
) {
    val typography = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
    val color = MaterialTheme.colorScheme.onPrimary

    Box(
        modifier = Modifier
            .orderConfigModifierHorizontal {
                selectColorTheme(ColorThemeOption.SYSTEM)
            }
            .fillMaxHeight()
            .weight(1F)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = contextDescription,
                //            stringResource(R.string.note_list),
                tint = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text, style = typography, color = color)
        }
    }
}
