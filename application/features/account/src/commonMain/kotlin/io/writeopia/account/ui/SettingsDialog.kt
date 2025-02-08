package io.writeopia.account.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.writeopia.common.utils.ResultData
import io.writeopia.common.utils.download.DownloadState
import io.writeopia.common.utils.icons.WrIcons
import io.writeopia.commonui.workplace.WorkspaceConfigurationDialog
import io.writeopia.model.ColorThemeOption
import io.writeopia.theme.WriteopiaTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

private const val SPACE_AFTER_TITLE = 12
private const val SPACE_AFTER_SUB_TITLE = 6

@Composable
fun SettingsDialog(
    workplacePathState: StateFlow<String>,
    selectedThemePosition: StateFlow<Int>,
    ollamaUrlState: StateFlow<String>,
    ollamaAvailableModels: Flow<ResultData<List<String>>>,
    ollamaSelectedModel: StateFlow<String>,
    downloadModelState: StateFlow<ResultData<DownloadState>>,
    onDismissRequest: () -> Unit,
    selectColorTheme: (ColorThemeOption) -> Unit,
    selectWorkplacePath: (String) -> Unit,
    ollamaUrlChange: (String) -> Unit,
    ollamaModelChange: (String) -> Unit,
    ollamaModelsRetry: () -> Unit,
    downloadModel: (String) -> Unit,
    deleteModel: (String) -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.7F),
//                .padding(horizontal = 40.dp, vertical = 20.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(24.dp).verticalScroll(rememberScrollState())
            ) {
                SettingsScreen(
                    showPath = true,
                    showOllamaConfig = true,
                    selectedThemePosition = selectedThemePosition,
                    workplacePathState = workplacePathState,
                    ollamaUrlState = ollamaUrlState,
                    ollamaAvailableModels = ollamaAvailableModels,
                    ollamaSelectedModel = ollamaSelectedModel,
                    downloadModelState = downloadModelState,
                    selectColorTheme = selectColorTheme,
                    selectWorkplacePath = selectWorkplacePath,
                    ollamaUrlChange = ollamaUrlChange,
                    ollamaModelChange = ollamaModelChange,
                    ollamaModelsRetry = ollamaModelsRetry,
                    downloadModel = downloadModel,
                    deleteModel = deleteModel,
                )
            }
        }
    }
}

@Composable
fun SettingsScreen(
    showPath: Boolean = true,
    showOllamaConfig: Boolean,
    selectedThemePosition: StateFlow<Int>,
    workplacePathState: StateFlow<String>,
    ollamaUrlState: StateFlow<String>,
    ollamaAvailableModels: Flow<ResultData<List<String>>>,
    ollamaSelectedModel: StateFlow<String>,
    downloadModelState: StateFlow<ResultData<DownloadState>>,
    selectColorTheme: (ColorThemeOption) -> Unit,
    selectWorkplacePath: (String) -> Unit,
    ollamaUrlChange: (String) -> Unit,
    ollamaModelChange: (String) -> Unit,
    ollamaModelsRetry: () -> Unit,
    downloadModel: (String) -> Unit,
    deleteModel: (String) -> Unit,
) {
    val titleStyle = MaterialTheme.typography.titleLarge
    val titleColor = MaterialTheme.colorScheme.onBackground
    val workplacePath by workplacePathState.collectAsState()
    var showEditPathDialog by remember {
        mutableStateOf(false)
    }

    Text("Color Theme", style = titleStyle, color = titleColor)

    Spacer(modifier = Modifier.height(SPACE_AFTER_TITLE.dp))

    ColorThemeOptions(
        selectedThemePosition = selectedThemePosition,
        selectColorTheme = selectColorTheme
    )

    Spacer(modifier = Modifier.height(20.dp))

    if (showPath) {
        Text("Local Folder", style = titleStyle, color = titleColor)

        Spacer(modifier = Modifier.height(SPACE_AFTER_TITLE.dp))

        val textShape = MaterialTheme.shapes.medium

        Text(
            workplacePath,
            style = MaterialTheme.typography.bodySmall,
            color = titleColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.border(
                1.dp,
                MaterialTheme.colorScheme.onSurfaceVariant,
                textShape
            )
                .clip(shape = textShape)
                .clickable {
                    showEditPathDialog = true
                }
                .padding(8.dp)
                .fillMaxWidth()
        )
    }

    if (showEditPathDialog) {
        WorkspaceConfigurationDialog(
            currentPath = workplacePath,
            pathChange = selectWorkplacePath,
            onDismissRequest = {
                showEditPathDialog = false
            },
            onConfirmation = {
                showEditPathDialog = false
            },
        )
    }

    Spacer(modifier = Modifier.height(20.dp))

    val modelsResult = ollamaAvailableModels.collectAsState(ResultData.Idle()).value
    val ollamaSelected by ollamaSelectedModel.collectAsState()

    if (showOllamaConfig) {
        Text("Ollama", style = titleStyle, color = titleColor)

        Spacer(modifier = Modifier.height(SPACE_AFTER_TITLE.dp))

        Text("Url", style = MaterialTheme.typography.bodyMedium, color = titleColor)

        Spacer(modifier = Modifier.height(SPACE_AFTER_SUB_TITLE.dp))

        val ollamaUrl by ollamaUrlState.collectAsState()

        BasicTextField(
            modifier = Modifier.border(
                1.dp,
                MaterialTheme.colorScheme.onSurfaceVariant,
                MaterialTheme.shapes.medium
            ).padding(10.dp)
                .fillMaxWidth(),
            value = ollamaUrl,
            onValueChange = ollamaUrlChange,
            textStyle = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onBackground
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground)
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text("Available Models", style = MaterialTheme.typography.bodyMedium, color = titleColor)

        Spacer(modifier = Modifier.height(SPACE_AFTER_SUB_TITLE.dp))

        when (modelsResult) {
            is ResultData.Complete -> {
                modelsResult.data.forEachIndexed { i, model ->

                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clip(MaterialTheme.shapes.medium)
                            .clickable {
                                ollamaModelChange(model)
                            }
                            .let { modifierLet ->
                                if (model == ollamaSelected) {
                                    modifierLet.background(WriteopiaTheme.colorScheme.highlight)
                                } else {
                                    modifierLet
                                }
                            }
                            .padding(8.dp)) {
                        Text(
                            modifier = Modifier.weight(1F),
                            text = model,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground,
                        )

                        if (!model.startsWith("No models")) {
                            Spacer(modifier = Modifier.width(6.dp))

                            Icon(
                                modifier = Modifier.clip(CircleShape)
                                    .clickable {
                                        deleteModel(model)
                                    }
                                    .padding(4.dp)
                                    .size(20.dp),
                                imageVector = WrIcons.delete,
                                contentDescription = "Trash can",
                                tint = Color.Red
                            )
                        }
                    }

                    if (i != modelsResult.data.lastIndex) {
                        Spacer(modifier = Modifier.height(2.dp))
                    }
                }
            }

            is ResultData.Error -> {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.medium)
                            .padding(8.dp),
                        text = "Error when requesting models. Did you start Ollama?",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground,
                    )

                    Text(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.medium)
                            .clickable(onClick = ollamaModelsRetry)
                            .background(
                                WriteopiaTheme.colorScheme.highlight,
                                MaterialTheme.shapes.medium
                            )
                            .padding(4.dp),
                        text = "Retry",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }

            is ResultData.Idle -> {}
            is ResultData.Loading, is ResultData.InProgress -> {
                CircularProgressIndicator()
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text("Download Model", style = MaterialTheme.typography.bodyMedium, color = titleColor)

        Spacer(modifier = Modifier.height(SPACE_AFTER_SUB_TITLE.dp))

        var modelToDownload by remember {
            mutableStateOf("")
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Suggestions:", style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.width(6.dp))

            listOf("deepseek-r1:7b", "llama3.2").forEach { model ->
                Text(
                    modifier = Modifier
                        .padding(horizontal = 1.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .clickable {
                            modelToDownload = model
                        }
                        .padding(8.dp),
                    text = model,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            BasicTextField(
                modifier = Modifier.border(
                    1.dp,
                    MaterialTheme.colorScheme.onSurfaceVariant,
                    MaterialTheme.shapes.medium
                )
                    .padding(10.dp)
                    .weight(1F),
                value = modelToDownload,
                onValueChange = { value ->
                    modelToDownload = value
                },
                textStyle = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onBackground
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                modifier = Modifier.size(34.dp)
                    .clip(CircleShape)
                    .clickable {
                        downloadModel(modelToDownload)
                    }.padding(6.dp),
                imageVector = WrIcons.download,
                contentDescription = "Download model",
                //            stringResource(R.string.note_list),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        when (val downloadState = downloadModelState.collectAsState().value) {
            is ResultData.Complete -> {}
            is ResultData.Error -> {
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    "Error when downloading model: ${downloadState.exception.message}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            is ResultData.Idle -> {}
            is ResultData.InProgress -> {
                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    val download = downloadState.data
                    Text(
                        download.title,
                        modifier = Modifier.weight(1F),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        download.info,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            is ResultData.Loading -> {
                CircularProgressIndicator()
            }
        }
    }

    Spacer(modifier = Modifier.height(30.dp))

    Text("Release version: alpha12", style = MaterialTheme.typography.bodySmall)
}

@Composable
private fun ColorThemeOptions(
    selectedThemePosition: StateFlow<Int>,
    selectColorTheme: (ColorThemeOption) -> Unit
) {
    val spaceWidth = 10.dp

    Row(modifier = Modifier.fillMaxWidth().height(70.dp)) {
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
    val typography = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
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
                modifier = Modifier.size(24.dp),
                imageVector = imageVector,
                contentDescription = contextDescription,
                //            stringResource(R.string.note_list),
                tint = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(text, style = typography, color = color)
        }
    }
}
