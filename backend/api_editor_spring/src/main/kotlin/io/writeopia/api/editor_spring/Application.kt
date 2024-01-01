package io.writeopia.api.editor_spring

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DocumentEditorApplication

fun main(args: Array<String>) {
    initFirebase()
    runApplication<DocumentEditorApplication>(*args)
}

private fun initFirebase() {
    val option = FirebaseOptions.builder()
        .setProjectId(loadProjectId())
        .setCredentials(GoogleCredentials.getApplicationDefault())
        .build()
    FirebaseApp.initializeApp(option)
}

private fun loadProjectId(): String = System.getenv("WRITEOPIA_CLOUD_ID")