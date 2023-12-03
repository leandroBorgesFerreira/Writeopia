package io.writeopia.note_menu.di

import io.writeopia.note_menu.data.repository.NotesConfigurationRepository

expect class NotesConfigurationInjector {

    fun provideNotesConfigurationRepository(): NotesConfigurationRepository
}