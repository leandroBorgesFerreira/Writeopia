package io.writeopia.api.editor_spring.config

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import io.writeopia.api.editor_spring.EditorHandler
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait
import org.springframework.web.reactive.function.server.coRouter


fun appRouter(editorHandler: EditorHandler) = coRouter {
    accept(MediaType.APPLICATION_JSON).nest {
        "/api/writeopia".nest {
            GET("/example") { request ->
                withAuth(request) {
                    ServerResponse.ok().bodyValueAndAwait(editorHandler.example())
                }
            }

            GET("/document/{id}") { request ->
                withAuth(request) {
                    editorHandler.getDocument(request.pathVariable("id"))
                }
            }

            POST("/document") { request ->
                withAuth(request) {
                    editorHandler.saveDocument(request.awaitBody())
                }
            }
        }
    }
}

suspend fun withAuth(request: ServerRequest, func: suspend () -> ServerResponse): ServerResponse {
    val idToken = request.headers()
        .firstHeader("Authorization")
        ?.replace("Bearer ", "")
        ?: return unAuthorized()

    return try {
        FirebaseAuth.getInstance().verifyIdToken(idToken)
        func()
    } catch (e: FirebaseAuthException) {
        unAuthorized()
    }
}

private suspend fun unAuthorized() = ServerResponse.status(HttpStatus.UNAUTHORIZED).buildAndAwait()
