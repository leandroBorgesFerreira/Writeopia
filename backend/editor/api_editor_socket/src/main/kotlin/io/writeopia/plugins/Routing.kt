package io.writeopia.plugins

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.ApplicationRequest
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.writeopia.api.editor.WriteopiaEditorApi
import io.writeopia.app.endpoints.EndPoints
import io.writeopia.sdk.serialization.data.DocumentApi
import io.writeopia.sdk.serialization.json.writeopiaJson
import kotlinx.serialization.encodeToString

fun Application.configureRouting(
    writeopiaEditorApi: WriteopiaEditorApi = WriteopiaEditorApi.create(),
    byPassAuth: Boolean = false
) {
    routing {
        get("/") {
            call.respondText("Hello")
        }

        get("/hi") {
            call.respondText("I'm alive")
        }

        route("/api") {
            get("/document/example") {
                call.respond(HttpStatusCode.OK, writeopiaEditorApi.example())
            }

            get("/${EndPoints.introNotes()}") {
                call.withAuth(byPass = byPassAuth) {
                    call.respond(HttpStatusCode.OK, writeopiaEditorApi.introNotes())
                }
            }

            get("/document/{id}") {
                call.withAuth(byPass = byPassAuth) {
                    call.parameters["id"]?.let { id ->
                        writeopiaEditorApi.getDocument(id)
                    }?.let { documentApi ->
                        call.respond(HttpStatusCode.OK, documentApi)
                    } ?: run {
                        call.respond(HttpStatusCode.NotFound, "Not Found")
                    }
                }
            }

            post("/document") {
                call.withAuth(byPass = byPassAuth) {
                    writeopiaEditorApi.saveDocument(call.receive<DocumentApi>())
                    call.respond(HttpStatusCode.Accepted)
                }
            }
        }
    }
}

suspend fun ApplicationCall.withAuth(
    byPass: Boolean = false,
    func: suspend () -> Unit
) {
    if (byPass) return func()

    val token = request.headers.run {
        this["X-Forwarded-Authorization"] ?: this["Authorization"]
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

private suspend fun ApplicationCall.unAuthorized(message: String = "Auth failed") =
    respond(HttpStatusCode.Unauthorized, message)
