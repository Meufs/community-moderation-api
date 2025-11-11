package snb.projects.persistence.services.users

import com.templates.domain.errors.ApplicationException
import snb.projects.domain.errors.ApplicationExceptionsEnum
import snb.projects.persistence.entities.MeufsEntity
import snb.projects.persistence.repositories.ClientsRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import snb.projects.domain.models.users.User
import snb.projects.domain.ports.out.FindClientsOut
import snb.projects.persistence.mappers.users.UsersEntityMapper

@ApplicationScoped
class FindClientsSpi: FindClientsOut {

    @Inject
    private lateinit var clientsRepository: ClientsRepository

    @Inject
    private lateinit var usersEntityMapper: UsersEntityMapper

    override fun findByIdentifier(identifier: String): User {
        val clientFromDb = clientsRepository.findByIdentifier(identifier).orElseThrow { ApplicationException(ApplicationExceptionsEnum.LOGIN_USER_NOT_FOUND) }
        val user = usersEntityMapper.fromClientToUser(clientFromDb)
        return user
    }

    override fun findByPasswordVerificationCode(passwordVerificationCode: String): User {
        return usersEntityMapper.fromClientToUser(clientsRepository.find("passwordVerificationCode", passwordVerificationCode)
            .firstResult<MeufsEntity>())
    }
}