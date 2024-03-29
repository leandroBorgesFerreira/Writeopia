package io.writeopia.api.editor_spring.config

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import io.writeopia.api.editor_spring.EditorHandler
import io.writeopia.app.endpoints.EndPoints
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.coRouter

fun appRouter(editorHandler: EditorHandler, byPassAuth: Boolean = false) = coRouter {
    accept(MediaType.APPLICATION_JSON).nest {
        "/api".nest {
            GET("/document/example") {
                editorHandler.example()
            }

            GET("/${EndPoints.introNotes()}") { request ->
                withAuth(request, byPass = byPassAuth) {
                    editorHandler.introNotes()
                }
            }

            GET("/document/{id}") { request ->
                withAuth(request, byPass = byPassAuth) {
                    editorHandler.getDocument(request.pathVariable("id"))
                }
            }

            GET("/${EndPoints.proxyUserNotes()}") { request ->
                withAuthProxy(request) { token ->
                    editorHandler.getProxyUserDocument(token)
                }
            }

            POST("/document") { request ->
                withAuth(request, byPass = byPassAuth) {
                    editorHandler.saveDocument(request.awaitBody())
                }
            }
        }
    }
}

suspend fun withAuth(
    request: ServerRequest,
    byPass: Boolean = false,
    func: suspend () -> ServerResponse
): ServerResponse {
    if (byPass) return func()

    val token = request.headers().run {
        firstHeader("X-Forwarded-Authorization") ?: firstHeader("Authorization")
    }

    val idToken = token?.replace("Bearer ", "")
        ?: return unAuthorized("The token was not correctly parsed")

    return try {
        FirebaseAuth.getInstance().verifyIdToken(idToken)
        func()
    } catch (e: FirebaseAuthException) {
        println("Unauthorized: ${e.message}")
        unAuthorized(e.message ?: "Auth failed")
    }
}

suspend fun withAuthProxy(
    request: ServerRequest,
    func: suspend (String) -> ServerResponse,
): ServerResponse {
    val token = request.headers().run {
        firstHeader("X-Forwarded-Authorization") ?: firstHeader("Authorization")
    }

    val idToken = token?.replace("Bearer ", "")
        ?: return unAuthorized("The token was not correctly parsed")

    return try {
        FirebaseAuth.getInstance().verifyIdToken(idToken)
        func(idToken)
    } catch (e: FirebaseAuthException) {
        println("Unauthorized: ${e.message}")
        unAuthorized(e.message ?: "Auth failed")
    }
}

private suspend fun unAuthorized(message: String = "Auth failed") =
    ServerResponse.status(HttpStatus.UNAUTHORIZED).bodyValueAndAwait(message)
