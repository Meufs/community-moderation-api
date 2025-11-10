package snb.projects.domain.mappers

import snb.projects.domain.models.users.User
import snb.projects.domain.models.users.UserCreated
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA)
fun interface UsersMappers {
    fun fromUsersToUsersLoggedIn(user: User, jwToken: String): UserCreated
}