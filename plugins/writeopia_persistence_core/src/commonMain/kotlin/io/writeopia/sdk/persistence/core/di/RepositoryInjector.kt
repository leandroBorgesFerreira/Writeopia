package io.writeopia.sdk.persistence.core.di

import io.writeopia.sdk.repository.DocumentRepository

interface RepositoryInjector {

    fun provideDocumentRepository(): DocumentRepository
}
