package snb.projects.domain.errors

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(val origin: String, val message: String)
