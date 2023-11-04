package io.writeopia.sdk.persistence.core.di

import io.writeopia.sdk.persistence.core.dao.DocumentDao

interface DaosInjector {

    fun provideDocumentDao(): DocumentDao
}