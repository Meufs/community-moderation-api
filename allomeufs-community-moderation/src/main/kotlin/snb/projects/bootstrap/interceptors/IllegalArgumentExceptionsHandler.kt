package snb.projects.bootstrap.interceptors

import io.quarkus.logging.Log
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider
import org.jboss.resteasy.reactive.RestResponse.StatusCode

@Provider
class IllegalArgumentExceptionsHandler : ExceptionMapper<IllegalArgumentException> {
    @Produces(MediaType.APPLICATION_JSON)
    override fun toResponse(e: IllegalArgumentException): Response {
        Log.info(e.toString())
        return Response.status(Response.Status.fromStatusCode(StatusCode.BAD_REQUEST))
            .entity(e.toString()).build()
    }
}