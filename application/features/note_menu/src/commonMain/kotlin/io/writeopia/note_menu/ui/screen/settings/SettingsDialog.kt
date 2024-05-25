package io.writeopia.note_menu.ui.screen.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Contrast
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.SystemUpdate
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.writeopia.model.ColorThemeOption
import io.writeopia.note_menu.ui.screen.configuration.modifier.orderConfigModifierHorizontal
import kotlinx.coroutines.flow.StateFlow

@Composable
fun SettingsDialog(
    selectedThemePosition: StateFlow<Int>,
    onDismissRequest: () -> Unit,
    selectColorTheme: (ColorThemeOption) -> Unit,
) {
    val titleStyle = MaterialTheme.typography.titleLarge
    val titleColor = MaterialTheme.colorScheme.onBackground

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.7F),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(modifier = Modifier.padding(horizontal = 40.dp, vertical = 20.dp)) {
                Text("Color Theme", style = titleStyle, color = titleColor)

                Spacer(modifier = Modifier.height(20.dp))

                ColorThemeOptions(
                    selectedThemePosition = selectedThemePosition,
                    selectColorTheme = selectColorTheme
                )
            }
        }
    }
}

@Composable
private fun ColorThemeOptions(
    selectedThemePosition: StateFlow<Int>,
    selectColorTheme: (ColorThemeOption) -> Unit
) {
    val typography = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
    val color = MaterialTheme.colorScheme.onBackground

    val spaceWidth = 10.dp

    Row(modifier = Modifier.fillMaxWidth().height(90.dp)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .orderConfigModifierHorizontal {
                    selectColorTheme(ColorThemeOption.LIGHT)
                }
                .weight(1F)
        ) {
            Icon(
                modifier = Modifier.weight(1F),
                imageVector = Icons.Outlined.LightMode,
                contentDescription = "staggered card",
                //            stringResource(R.string.staggered_card),
                tint = MaterialTheme.colorScheme.onPrimary
            )

            Text("Light", style = typography, color = color)
        }

        Spacer(modifier = Modifier.width(spaceWidth))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .orderConfigModifierHorizontal {
                    selectColorTheme(ColorThemeOption.DARK)
                }
                .weight(1F)
        ) {
            Icon(
                modifier = Modifier.weight(1F),
                imageVector = Icons.Outlined.DarkMode,
                contentDescription = "staggered card",
                //            stringResource(R.string.staggered_card),
                tint = MaterialTheme.colorScheme.onPrimary
            )

            Text("Dark", style = typography, color = color)
        }

        Spacer(modifier = Modifier.width(spaceWidth))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .orderConfigModifierHorizontal {
                    selectColorTheme(ColorThemeOption.SYSTEM)
                }
                .weight(1F)
        ) {
            Icon(
                modifier = Modifier.weight(1F),
                imageVector = Icons.Outlined.Contrast,
                contentDescription = "note list",
                //            stringResource(R.string.note_list),
                tint = MaterialTheme.colorScheme.onPrimary
            )

            Text("System", style = typography, color = color)
        }
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
