package snb.projects.application.dto.responses

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(val origin: String, val message: String)
