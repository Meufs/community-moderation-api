package snb.projects.application.controllers

import snb.projects.application.dto.requests.CreateAdminRequest
import snb.projects.application.dto.requests.CreateUserRequest
import snb.projects.application.dto.responses.CreateUserResponse
import snb.projects.application.mappers.UsersDtoMappers
import io.quarkus.logging.Log
import jakarta.annotation.security.PermitAll
import jakarta.enterprise.context.RequestScoped
import jakarta.inject.Inject
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.media.Content
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.jboss.resteasy.reactive.ResponseStatus
import org.jboss.resteasy.reactive.RestResponse.StatusCode.CREATED
import snb.projects.domain.ports.`in`.CreateUsersIn
import snb.projects.domain.ports.`in`.CsrfTokenGeneratorIn


@Path("/users-create")
@RequestScoped
class CreateUsersResource {

    @Inject
    private lateinit var createUsersIn: CreateUsersIn

    @Inject
    private lateinit var cookieUtils: CookieUtils

    @Inject
    private lateinit var usersDtoMappers: UsersDtoMappers

    @Inject
    private lateinit var csrfTokenGeneratorIn: CsrfTokenGeneratorIn

    @ConfigProperty(name="quarkus.rest-csrf.cookie-name")
    private lateinit var csrfCookieName: String

    @POST
    @Path("/meufs")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ResponseStatus(CREATED)
    @PermitAll
    @Operation(summary = "Create a meuf", description = "Create a meuf")
    @APIResponses(
        APIResponse(responseCode = "200", description = "OK", content = [Content(mediaType = "application/json",
            schema = Schema(implementation = CreateUserResponse::class)
        )]),
    )
    fun createClient(creationRequest: CreateUserRequest): Response {
        Log.info("Creating client")
        val mappedRequest = usersDtoMappers.fromCreationRequest(creationRequest)
        Log.debug(String.format("Creating user %s %s", mappedRequest.firstName, mappedRequest.lastName))
        val userCreationInformations = createUsersIn.createUser(mappedRequest)
        val bearerCookie = cookieUtils.setUpCookie("Bearer", userCreationInformations.jwToken)
        val csrfToken = csrfTokenGeneratorIn.generateToken(mappedRequest.mail)
        val csrfCookie = cookieUtils.setUpCookie(csrfCookieName, csrfToken)
        return Response.ok(usersDtoMappers.toCreationResponse(userCreationInformations)).cookie(bearerCookie).cookie(csrfCookie).build()
    }

    @POST
    @Path("/admin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ResponseStatus(CREATED)
    @PermitAll
    @Operation(summary = "Create an admin", description = "Create an admin")
    @APIResponses(
        APIResponse(responseCode = "200", description = "OK", content = [Content(mediaType = "application/json",
            schema = Schema(implementation = CreateUserResponse::class)
        )]),
    )
    fun createAdmin(creationRequest: CreateAdminRequest): Response {
        Log.info("Creating admin")
        Log.info(creationRequest.toString())
        val mappedRequest = usersDtoMappers.fromCreationRequest(creationRequest)
        Log.info(String.format("Creating admin %s %s", mappedRequest.firstName, mappedRequest.lastName))
        val userCreationInformations = createUsersIn.createAdmin(mappedRequest, creationRequest.adminCode)
        Log.info("Setting cookies")
        val bearerCookie = cookieUtils.setUpCookie("Bearer", userCreationInformations.jwToken)
        val csrfToken = csrfTokenGeneratorIn.generateToken(mappedRequest.mail)
        val csrfCookie = cookieUtils.setUpCookie(csrfCookieName, csrfToken)
        return Response.ok(usersDtoMappers.toCreationResponse(userCreationInformations)).cookie(bearerCookie).cookie(csrfCookie).build()
    }
}