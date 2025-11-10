package snb.projects.application.dto.requests

import kotlinx.serialization.Serializable

@Serializable
data class CreateAdminRequest(
    val firstName: String,
    val lastName: String,
    val mail: String,
    val password: String,
    val phoneNumber: String,
    val adminCode: String,
)
