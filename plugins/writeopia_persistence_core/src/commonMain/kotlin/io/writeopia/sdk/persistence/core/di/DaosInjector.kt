package io.writeopia.sdk.persistence.core.di

import io.writeopia.sdk.persistence.core.dao.DocumentRepository

interface DaosInjector {

    fun provideDocumentDao(): DocumentRepository

    companion object
}