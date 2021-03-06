package dev.psuchanek

import dev.psuchanek.database.checkPasswordForEmail
import dev.psuchanek.routes.deleteAccountRoutes
import dev.psuchanek.routes.flightRoutes
import dev.psuchanek.routes.loginRoute
import dev.psuchanek.routes.registerRoute
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.routing.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
    install(Authentication) {
        configureAuth()
    }
    install(Routing) {
        registerRoute()
        loginRoute()
        deleteAccountRoutes()
        flightRoutes()
    }
}

private fun Authentication.Configuration.configureAuth() {
    basic {
        realm = "Logbook Server"
        validate { credentials ->
            val email = credentials.name
            val password = credentials.password
            if (checkPasswordForEmail(email, password)) {
                UserIdPrincipal(email)
            } else null
        }
    }
}

