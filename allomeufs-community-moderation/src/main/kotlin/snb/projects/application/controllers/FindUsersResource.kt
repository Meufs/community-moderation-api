package com.templates.application.controllers

import jakarta.annotation.security.RolesAllowed
import jakarta.enterprise.context.RequestScoped
import jakarta.enterprise.inject.Default
import jakarta.inject.Inject
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.jwt.JsonWebToken
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import io.quarkus.logging.Log;
import org.jboss.resteasy.reactive.ResponseStatus
import org.jboss.resteasy.reactive.RestResponse.StatusCode.CREATED


@Path("/users-find")
@RequestScoped
class FindUsersResource {

    @Inject
    private lateinit var jwt: JsonWebToken

    @GET
    @Path("/me")
    @Consumes(MediaType.APPLICATION_JSON)
    @ResponseStatus(CREATED)
    @RolesAllowed("MEUF","MODO")
    @SecurityRequirement(name = "bearer")
    fun getMe() {
        val userMail = jwt.name
        Log.info(userMail)
    }
}