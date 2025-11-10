package snb.projects.domain.ports.`in`

import snb.projects.domain.models.commands.users.CreateUserCommand
import snb.projects.domain.models.users.UserBasicInformations

interface CreateUsersIn {
    fun createUser(user: CreateUserCommand): UserBasicInformations
    fun createAdmin(user: CreateUserCommand, adminCode:String): UserBasicInformations

}