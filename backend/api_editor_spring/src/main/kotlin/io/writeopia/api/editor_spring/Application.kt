package io.writeopia.api.editor_spring

import com.google.firebase.FirebaseApp
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DocumentEditorApplication

fun main(args: Array<String>) {
    FirebaseApp.initializeApp();
    runApplication<DocumentEditorApplication>(*args)
}