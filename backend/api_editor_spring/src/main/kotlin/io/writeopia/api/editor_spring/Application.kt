package io.writeopia.api.editor_spring

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.FileInputStream
import java.util.Properties


@SpringBootApplication
class DocumentEditorApplication

fun main(args: Array<String>) {
    val option = FirebaseOptions.builder()
        .setProjectId(loadProjectId())
        .setCredentials(GoogleCredentials.getApplicationDefault())
        .build()
    FirebaseApp.initializeApp(option)

    runApplication<DocumentEditorApplication>(*args)
}

private fun loadProjectId(): String = System.getenv("WRITEOPIA_CLOUD_ID")