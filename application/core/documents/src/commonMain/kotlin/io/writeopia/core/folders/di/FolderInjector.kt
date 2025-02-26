package io.writeopia.core.folders.di

import io.writeopia.core.folders.repository.FolderRepository

interface FolderInjector {

    fun provideFoldersRepository(): FolderRepository
}
