package snb.projects.application.controllers

import jakarta.annotation.security.RolesAllowed
import jakarta.enterprise.context.RequestScoped
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.eclipse.microprofile.jwt.JsonWebToken
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.media.Content
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.jboss.resteasy.reactive.ResponseStatus
import org.jboss.resteasy.reactive.RestResponse.StatusCode.OK
import snb.projects.domain.ports.`in`.CsrfTokenGeneratorIn

@Path("/test")
@RequestScoped
class TestCsrf {
    @Inject
    private lateinit var cookieUtils: CookieUtils

    @Inject
    private lateinit var jwt: JsonWebToken
    @Inject
    private lateinit var csrfTokenGeneratorIn: CsrfTokenGeneratorIn

    @field:ConfigProperty(name="quarkus.rest-csrf.cookie-name")
    private lateinit var csrfCookieName: String

    @GET
    @Path("/test-csrf")
    @ResponseStatus(OK)
    @RolesAllowed("MEUF","ADMIN","MODO")
    @SecurityRequirement(name = "bearer")
    @Operation(summary = "Update user profile picture", description = "Modifies the user profile picture, update the " +
            "link in db, returns the new url")
    @APIResponses(
        APIResponse(responseCode = "200", description = "OK", content = [Content(mediaType = "text/plain",
            schema = Schema(implementation = String::class)
        )]),
    )
    fun ttt(): Response {
        val userMail = jwt.name
        val csrfToken = csrfTokenGeneratorIn.generateToken(userMail)
        val csrfCookie = cookieUtils.setUpCookie(csrfCookieName, csrfToken)
        return Response.ok("LOL").cookie(csrfCookie).build()
    }
}