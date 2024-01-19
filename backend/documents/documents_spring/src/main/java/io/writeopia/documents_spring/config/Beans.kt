package io.writeopia.documents_spring.config

import io.writeopia.documents_spring.DocumentsHandler
import io.writeopia.documents_spring.api.DocumentsRepository
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans

val beans = beans {
    bean<DocumentsRepository> {
        DocumentsRepository()
    }

    bean {
        DocumentsHandler(ref())
    }

    bean {
        appRouter(ref())
    }
}

class BeansInitializer : ApplicationContextInitializer<GenericApplicationContext> {
    override fun initialize(context: GenericApplicationContext) {
        beans.initialize(context)
    }

}
