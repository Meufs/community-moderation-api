package snb.projects.application.dto.requests

import kotlinx.serialization.Serializable

@Serializable
data class InitPasswordRecoveryRequest(val identifier:String)
