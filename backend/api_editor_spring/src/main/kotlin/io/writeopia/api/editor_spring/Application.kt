package io.writeopia.api.editor_spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DocumentEditorApplication

fun main(args: Array<String>) {
    runApplication<DocumentEditorApplication>(*args)
}