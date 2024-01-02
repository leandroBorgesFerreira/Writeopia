package io.writeopia.api.editor_spring.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.writeopia.api.editor.WriteopiaEditorApi
import io.writeopia.api.editor_spring.EditorHandler
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans

val beans = beans {
    bean<WriteopiaEditorApi> {
        WriteopiaEditorApi.create()
    }

    bean<EditorHandler> {
        EditorHandler(ref())
    }

    bean {
        appRouter(ref<EditorHandler>())
    }
}

class BeansInitializer : ApplicationContextInitializer<GenericApplicationContext> {
    override fun initialize(context: GenericApplicationContext) {
        beans.initialize(context)
    }

}

private fun initFirebase() {
    val option = FirebaseOptions.builder()
        .setProjectId(loadProjectId())
        .setCredentials(GoogleCredentials.getApplicationDefault())
        .build()
    FirebaseApp.initializeApp(option)
}

private fun loadProjectId(): String = System.getenv("WRITEOPIA_CLOUD_ID")

class FirebaseInitializer: ApplicationContextInitializer<GenericApplicationContext> {
    override fun initialize(applicationContext: GenericApplicationContext) {
        initFirebase()
    }
}