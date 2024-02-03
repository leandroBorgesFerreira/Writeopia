package io.writeopia.api.editor_spring

import io.writeopia.api.editor_spring.config.BeansInitializer
import io.writeopia.api.editor_spring.config.FirebaseInitializer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication
class DocumentEditorApplication

fun main(args: Array<String>) {
    SpringApplicationBuilder(DocumentEditorApplication::class.java)
        .initializers(BeansInitializer(), FirebaseInitializer())
        .build()
        .run(*args)
}

