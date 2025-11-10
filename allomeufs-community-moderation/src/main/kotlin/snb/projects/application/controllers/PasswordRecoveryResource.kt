package snb.projects.application.controllers

import io.quarkus.logging.Log
import jakarta.annotation.security.PermitAll
import jakarta.annotation.security.RolesAllowed
import jakarta.enterprise.context.RequestScoped
import jakarta.inject.Inject
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.PUT
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.eclipse.microprofile.jwt.JsonWebToken
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.jboss.resteasy.reactive.ResponseStatus
import org.jboss.resteasy.reactive.RestResponse.StatusCode.NO_CONTENT
import snb.projects.application.dto.requests.InitPasswordRecoveryRequest
import snb.projects.application.dto.requests.PasswordChangeRequest
import snb.projects.application.dto.requests.PasswordRecoveryRequest
import snb.projects.domain.ports.`in`.CsrfTokenGeneratorIn
import snb.projects.domain.ports.`in`.PasswordManagementIn

@Path("/password-recovery")
@RequestScoped
class PasswordRecoveryResource {

    @Inject
    private lateinit var passwordManagementIn: PasswordManagementIn

    @Inject
    private lateinit var jwt:JsonWebToken

    @Inject
    private lateinit var csrfTokenGeneratorIn: CsrfTokenGeneratorIn

    @field:ConfigProperty(name="quarkus.rest-csrf.cookie-name")
    private lateinit var csrfCookieName: String
    @Inject
    private lateinit var cookieUtils: CookieUtils
    @PUT
    @Path("/init-reset")
    @Consumes(MediaType.APPLICATION_JSON)
    @ResponseStatus(NO_CONTENT)
    @PermitAll
    @APIResponses(
            APIResponse(responseCode = "204", description = "NO_CONTENT")
    )
    fun initiatePasswordReset(passwordRecoveryRequest: InitPasswordRecoveryRequest) {
        passwordManagementIn.initPasswordRecovery(passwordRecoveryRequest.identifier)
    }

    @PUT
    @Path("/reset-password")
    @Consumes(MediaType.APPLICATION_JSON)
    @ResponseStatus(NO_CONTENT)
    @PermitAll
    fun resetPassword(passwordRecoveryRequest: PasswordRecoveryRequest) {
        passwordManagementIn.recoverPassword(passwordRecoveryRequest.mail, passwordRecoveryRequest.token,
            passwordRecoveryRequest.password,
            passwordRecoveryRequest.passwordConfirmation)
    }

    @PUT
    @Path("/change-password")
    @Consumes(MediaType.APPLICATION_JSON)
    @ResponseStatus(NO_CONTENT)
    @RolesAllowed("CLIENT","ADMIN")
    fun changePassword(passwordChangeRequest: PasswordChangeRequest): Response {
        Log.info("Starting update password")
        val mail = jwt.name
        passwordManagementIn.changePassword(mail, passwordChangeRequest.password, passwordChangeRequest.passwordConfirmation)
        val csrfToken = csrfTokenGeneratorIn.generateToken(mail)
        val csrfCookie = cookieUtils.setUpCookie(csrfCookieName, csrfToken)
        return Response.noContent().cookie(csrfCookie).build()
    }
}