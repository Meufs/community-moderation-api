package snb.projects.bootstrap.interceptors

import com.templates.domain.errors.ApplicationException
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider
import snb.projects.domain.errors.ErrorResponse

@Provider
class ApplicationExceptionsHandler : ExceptionMapper<ApplicationException> {
    override fun toResponse(e: ApplicationException): Response {
        val errorResponse = ErrorResponse(e.origin, e.message)
        return Response.status(Response.Status.fromStatusCode(e.statusCode))
            .entity(errorResponse).build()
    }
}