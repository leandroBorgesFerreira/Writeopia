package io.writeopia.documents_spring.config

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import io.writeopia.app.endpoints.EndPoints
import io.writeopia.documents_spring.DocumentsHandler
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.coRouter

fun appRouter(documentsHandler: DocumentsHandler) = coRouter {
    accept(MediaType.APPLICATION_JSON).nest {
        "/api".nest {

            GET("/${EndPoints.userNotes()}") {
                documentsHandler.userDocuments()
            }
        }
    }
}

suspend fun withAuth(request: ServerRequest, func: suspend () -> ServerResponse): ServerResponse {
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

private suspend fun unAuthorized(message: String = "Auth failed") =
    ServerResponse.status(HttpStatus.UNAUTHORIZED).bodyValueAndAwait(message)
