package snb.projects.persistence.services.users

import com.templates.domain.errors.ApplicationException
import com.templates.domain.errors.ApplicationExceptionsEnum
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import snb.projects.domain.models.users.User
import snb.projects.domain.ports.out.FindUserOut
import snb.projects.persistence.mappers.users.UsersEntityMapper
import snb.projects.persistence.repositories.UsersRepository

@ApplicationScoped
class FindUsersSpi: FindUserOut {

    @Inject
    private lateinit var usersRepository: UsersRepository

    @Inject
    private lateinit var usersEntityMapper: UsersEntityMapper

    override fun findByIdentifier(identifier: String): User {
        val userInDb = usersRepository.findByIdentifier(identifier).orElseThrow { ApplicationException(
            ApplicationExceptionsEnum.LOGIN_USER_NOT_FOUND) }
        val user = usersEntityMapper.fromEntityToUser(userInDb)
        return user
    }
}