package com.zenmo.plugins;

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.html.*
import io.ktor.util.*
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.p

val applicationHttpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json()
    }
}

fun Application.configureAuthentication() {
    install(Sessions) {
        cookie<UserSession>("user_session")
    }
    val redirects = mutableMapOf<String, String>()
    install(Authentication) {
        oauth("auth-oauth-ory") {
            // We could make a call here to register the base url in Ory Hydra
            // for the test enviroment which has a dynamic domain.
            urlProvider = { System.getenv("BASE_URL") + "/callback" }
            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = "ory",
                    authorizeUrl = "https://auth.zenmo.com/oauth2/auth",
                    accessTokenUrl = "https://auth.zenmo.com/oauth2/token",
                    requestMethod = HttpMethod.Post,
                    clientId = System.getenv("ORY_OAUTH_CLIENT_ID"),
                    clientSecret = System.getenv("ORY_OAUTH_CLIENT_SECRET"),
                    defaultScopes = listOf("openid", "profile", "email"),
                    onStateCreated = { call, state ->
                        //saves new state with redirect url value
                        call.request.queryParameters["redirectUrl"]?.let {
                            redirects[state] = it
                        }
                    }
                )
            }
            client = applicationHttpClient
        }
    }
    routing {
        authenticate("auth-oauth-ory") {
            get("/login") {
                // Redirects to 'authorizeUrl' automatically
            }

            get("/callback") {
                val currentPrincipal: OAuthAccessTokenResponse.OAuth2? = call.principal()
                if (currentPrincipal == null) {
                    call.respondText("No principal")
                    return@get
                }
                call.respondText(currentPrincipal.accessToken)
                // redirects home if the url is not found before authorization
                currentPrincipal?.let { principal ->
                    principal.state?.let { state ->
                        call.sessions.set(UserSession(state, principal.accessToken))
                        redirects[state]?.let { redirect ->
                            call.respondRedirect(redirect)
                            return@get
                        }
                    }
                }
                call.respondRedirect("/home")
            }
        }
        get("/home") {
            val userSession: UserSession? = call.sessions.get()
            if (userSession != null) {
                call.respondText("Hello, user! Welcome home!")
            } else {
                call.respondHtml {
                    body {
                        p {
                            a("/login") { +"Login with Ory" }
                        }
                    }
                }
            }
        }
    }
}

data class UserSession(val state: String, val token: String)
