package io.writeopia.documents_spring

import io.writeopia.documents_spring.config.BeansInitializer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication
class DocumentEditorApplication

fun main(args: Array<String>) {
    SpringApplicationBuilder(DocumentEditorApplication::class.java)
        .initializers(BeansInitializer())
        .build()
        .run(*args)
}

