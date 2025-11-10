package snb.projects.application.dto.responses

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserResponse(val type:String, val reference: String,
                              val accountVerifiedStatus:Boolean)


