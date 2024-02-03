package io.writeopia.api.editor_spring.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.writeopia.api.editor.WriteopiaEditorApi
import io.writeopia.api.editor_spring.EditorHandler
import io.writeopia.api.editor_spring.auth.FixedTokenHandler
import io.writeopia.sdk.network.injector.ApiClientInjector
import io.writeopia.sdk.network.notes.NotesApi
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

internal fun notesApiFromToken(token: String): NotesApi =
    ApiClientInjector(
        bearerTokenHandler = FixedTokenHandler(token),
        baseUrl = System.getenv("WRITEOPIA_BASE_URL")
    ).notesApi()

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

