package io.writeopia.note_menu.di

import io.writeopia.note_menu.data.repository.ConfigurationRepository

expect class NotesConfigurationInjector {

    fun provideNotesConfigurationRepository(): ConfigurationRepository
}