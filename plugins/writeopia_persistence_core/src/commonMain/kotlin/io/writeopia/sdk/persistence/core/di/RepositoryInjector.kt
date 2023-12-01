package io.writeopia.sdk.persistence.core.di

import io.writeopia.sdk.persistence.core.dao.DocumentRepository

interface RepositoryInjector {

    fun provideDocumentRepository(): DocumentRepository

    companion object
}