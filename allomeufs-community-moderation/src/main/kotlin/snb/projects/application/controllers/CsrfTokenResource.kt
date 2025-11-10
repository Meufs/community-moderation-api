package snb.projects.application.controllers

import jakarta.annotation.security.RolesAllowed
import jakarta.enterprise.context.RequestScoped
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.jwt.JsonWebToken
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.jboss.resteasy.reactive.ResponseStatus
import org.jboss.resteasy.reactive.RestResponse.StatusCode.ACCEPTED
import snb.projects.domain.ports.`in`.CsrfTokenGeneratorIn

@Path("/csrf-token")
@RequestScoped
class CsrfTokenResource {
    @Inject
    private lateinit var jwt: JsonWebToken
    @Inject
    private lateinit var csrfTokenGeneratorIn: CsrfTokenGeneratorIn
    @Inject
    private lateinit var cookieUtils: CookieUtils
    @GET
    @ResponseStatus(ACCEPTED)
    @RolesAllowed("MEUF","ADMIN","MODO")
    @SecurityRequirement(name = "bearer")
    fun getCsrfToken( ): Response {
        val userMail = jwt.name
        val csrfToken = csrfTokenGeneratorIn.generateToken(userMail)
        val cookie = cookieUtils.setUpCookie("csrf-token", csrfToken)
        return Response.ok().cookie(cookie).build()
    }
}