package snb.projects.bootstrap.configuration

import snb.projects.domain.services.CsrfTokenCache
import jakarta.annotation.Priority
import jakarta.enterprise.inject.Default
import jakarta.inject.Inject
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerRequestFilter
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.Provider
import org.eclipse.microprofile.jwt.JsonWebToken

@Provider
@Priority(1)
class CsrfCookieFilter:ContainerRequestFilter {

    @Inject
    private lateinit var csrfTokenCache: CsrfTokenCache

    @Inject
    private lateinit var jwt: JsonWebToken

    override fun filter(requestContext: ContainerRequestContext) {
        val csrfCookie = requestContext.cookies["csrf-token"]
        val mail = jwt.name
        if(requestContext.uriInfo.path.startsWith
                ("/admin-code")|| requestContext.uriInfo.path.startsWith
                ("/csrf-token") || requestContext.uriInfo.path.startsWith
                ("/connexion") || requestContext.uriInfo.path.startsWith
                ("/users-create")|| requestContext.uriInfo.path.startsWith
                ("/healthcheck")|| requestContext.uriInfo.path.startsWith
                ("/hello")|| requestContext.uriInfo.path.startsWith
                ("/connection")|| requestContext.uriInfo.path == "/password-recovery/init-reset"
            || requestContext.uriInfo.path ==
                "/password-recovery/reset-password"){
            return
        }
        if (csrfCookie == null || csrfCookie.value.isEmpty() || csrfCookie.value != csrfTokenCache
            .getUserToken(mail)) {
            requestContext.abortWith(
                Response.status(Response.Status.EXPECTATION_FAILED)
                .entity("CSRF token error").build())
        }
    }
}