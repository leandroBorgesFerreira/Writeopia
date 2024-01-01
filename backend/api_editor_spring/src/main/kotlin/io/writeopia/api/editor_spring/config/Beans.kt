package io.writeopia.api.editor_spring.config

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
    override fun initialize(context: GenericApplicationContext) = beans.initialize(context)
}