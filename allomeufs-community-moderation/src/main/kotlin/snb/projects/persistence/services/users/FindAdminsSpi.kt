package snb.projects.persistence.services.users

import com.templates.domain.errors.ApplicationException
import com.templates.domain.errors.ApplicationExceptionsEnum
import com.templates.persistence.repositories.AdminsRepository
import io.quarkus.logging.Log
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import snb.projects.domain.models.users.User
import snb.projects.domain.ports.out.FindAdminsOut
import snb.projects.persistence.mappers.users.UsersEntityMapper

@ApplicationScoped
class FindAdminsSpi : FindAdminsOut {

    @Inject
    private lateinit var adminsRepository: AdminsRepository

    @Inject
    private lateinit var usersEntityMapper: UsersEntityMapper

    override fun findByIdentifier(identifier: String): User {
        val clientFromDb = adminsRepository.findByIdentifier(identifier).orElseThrow { ApplicationException(
            ApplicationExceptionsEnum.LOGIN_USER_NOT_FOUND) }
        Log.debug(clientFromDb.toString())
        val user = usersEntityMapper.fromAdminToUser(clientFromDb)
        Log.debug(user.toString())
        return user
    }
}