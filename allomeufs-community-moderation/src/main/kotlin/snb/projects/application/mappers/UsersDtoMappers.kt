package snb.projects.application.mappers

import snb.projects.application.dto.requests.CreateAdminRequest
import snb.projects.application.dto.requests.CreateUserRequest
import snb.projects.application.dto.responses.CreateUserResponse
import snb.projects.application.dto.responses.UserLoginResponse
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants
import snb.projects.domain.models.commands.users.CreateUserCommand
import snb.projects.domain.models.users.UserBasicInformations
import snb.projects.domain.models.users.UserCreated

@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
interface UsersDtoMappers {
    @Mapping(target = "reference", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "verificationCode", ignore = true)
    @Mapping(target = "verificationCodeTimestamp", ignore = true)
    fun fromCreationRequest(createUserRequest: CreateUserRequest): CreateUserCommand

    @Mapping(target = "reference", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "verificationCode", ignore = true)
    @Mapping(target = "verificationCodeTimestamp", ignore = true)
    fun fromCreationRequest(createAdminRequest: CreateAdminRequest):CreateUserCommand

    fun toCreationResponse(userBasicInformations: UserBasicInformations):CreateUserResponse
    fun toLoginResponse(user: UserCreated): UserLoginResponse

}