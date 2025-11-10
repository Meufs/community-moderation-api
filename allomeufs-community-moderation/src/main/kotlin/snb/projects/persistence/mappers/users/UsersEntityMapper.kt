package snb.projects.persistence.mappers.users

import snb.projects.persistence.entities.AdminsEntity
import snb.projects.persistence.entities.MeufsEntity
import com.templates.persistence.entities.UsersEntity
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants
import snb.projects.domain.models.commands.users.CreateUserCommand
import snb.projects.domain.models.users.User

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA)
interface UsersEntityMapper {
    fun fromCreateUserToEntity(createUserCommand: CreateUserCommand): UsersEntity
    fun fromCreateUserToClient(createUserCommand: CreateUserCommand): MeufsEntity
    fun fromCreateUserToAdmin(createUserCommand: CreateUserCommand): AdminsEntity

    fun fromEntityToUser(usersEntity: UsersEntity): User
    fun fromClientToUser(meufsEntity: MeufsEntity): User

    fun fromAdminToUser(adminsEntity: AdminsEntity): User
    fun fromUserToEntity(user: User): UsersEntity

}