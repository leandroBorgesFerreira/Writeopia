package io.writeopia.resources

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import writeopia.application.core.resources.generated.resources.Res
import writeopia.application.core.resources.generated.resources.available_models
import writeopia.application.core.resources.generated.resources.box
import writeopia.application.core.resources.generated.resources.color_theme
import writeopia.application.core.resources.generated.resources.dark_theme
import writeopia.application.core.resources.generated.resources.decoration
import writeopia.application.core.resources.generated.resources.download_models
import writeopia.application.core.resources.generated.resources.error_loading_notes
import writeopia.application.core.resources.generated.resources.error_model_download
import writeopia.application.core.resources.generated.resources.error_requesting_models
import writeopia.application.core.resources.generated.resources.export_markdown
import writeopia.application.core.resources.generated.resources.favorites
import writeopia.application.core.resources.generated.resources.folder
import writeopia.application.core.resources.generated.resources.home
import writeopia.application.core.resources.generated.resources.import_file
import writeopia.application.core.resources.generated.resources.insert
import writeopia.application.core.resources.generated.resources.light_theme
import writeopia.application.core.resources.generated.resources.local_folder
import writeopia.application.core.resources.generated.resources.lock_document
import writeopia.application.core.resources.generated.resources.move_to
import writeopia.application.core.resources.generated.resources.move_to_home
import writeopia.application.core.resources.generated.resources.no_models
import writeopia.application.core.resources.generated.resources.ollama
import writeopia.application.core.resources.generated.resources.recent
import writeopia.application.core.resources.generated.resources.retry
import writeopia.application.core.resources.generated.resources.search
import writeopia.application.core.resources.generated.resources.settings
import writeopia.application.core.resources.generated.resources.sort_by_creation
import writeopia.application.core.resources.generated.resources.sort_by_name
import writeopia.application.core.resources.generated.resources.sort_by_update
import writeopia.application.core.resources.generated.resources.suggestions
import writeopia.application.core.resources.generated.resources.system_theme
import writeopia.application.core.resources.generated.resources.text
import writeopia.application.core.resources.generated.resources.url
import writeopia.application.core.resources.generated.resources.version

object WrStrings {

    @Composable
    fun search() = stringResource(Res.string.search)

    @Composable
    fun home() = stringResource(Res.string.home)

    @Composable
    fun favorites() = stringResource(Res.string.favorites)

    @Composable
    fun settings() = stringResource(Res.string.settings)

    @Composable
    fun folder() = stringResource(Res.string.folder)

    @Composable
    fun recent() = stringResource(Res.string.recent)

    @Composable
    fun colorTheme() = stringResource(Res.string.color_theme)

    @Composable
    fun localFolder() = stringResource(Res.string.local_folder)

    @Composable
    fun ollama() = stringResource(Res.string.ollama)

    @Composable
    fun url() = stringResource(Res.string.url)

    @Composable
    fun availableModels() = stringResource(Res.string.available_models)

    @Composable
    fun noModelsFound() = stringResource(Res.string.no_models)

    @Composable
    fun errorRequestingModels() = stringResource(Res.string.error_requesting_models)

    @Composable
    fun retry() = stringResource(Res.string.retry)

    @Composable
    fun downloadModels() = stringResource(Res.string.download_models)

    @Composable
    fun suggestions() = stringResource(Res.string.suggestions)

    @Composable
    fun errorModelDownload() = stringResource(Res.string.error_model_download)

    @Composable
    fun version() = stringResource(Res.string.version)

    @Composable
    fun lightTheme() = stringResource(Res.string.light_theme)

    @Composable
    fun darkTheme() = stringResource(Res.string.dark_theme)

    @Composable
    fun systemTheme() = stringResource(Res.string.system_theme)

    @Composable
    fun exportMarkdown() = stringResource(Res.string.export_markdown)

    @Composable
    fun importFile() = stringResource(Res.string.import_file)

    @Composable
    fun sortByName() = stringResource(Res.string.sort_by_name)

    @Composable
    fun sortByCreation() = stringResource(Res.string.sort_by_creation)

    @Composable
    fun sortByUpdate() = stringResource(Res.string.sort_by_update)

    @Composable
    fun lockDocument() = stringResource(Res.string.lock_document)

    @Composable
    fun moveTo() = stringResource(Res.string.move_to)

    @Composable
    fun moveToHome() = stringResource(Res.string.move_to_home)

    @Composable
    fun text() = stringResource(Res.string.text)

    @Composable
    fun insert() = stringResource(Res.string.insert)

    @Composable
    fun decoration() = stringResource(Res.string.decoration)

    @Composable
    fun box() = stringResource(Res.string.box)
}
