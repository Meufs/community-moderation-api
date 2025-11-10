package snb.projects.persistence.services.users

import com.templates.domain.errors.ApplicationException
import com.templates.domain.errors.ApplicationExceptionsEnum
import com.templates.domain.ports.out.CreateUsersOut
import com.templates.persistence.repositories.AdminsRepository
import io.quarkus.logging.Log
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.hibernate.exception.ConstraintViolationException
import snb.projects.domain.models.commands.users.CreateUserCommand
import snb.projects.persistence.mappers.users.UsersEntityMapper
import snb.projects.persistence.repositories.ClientsRepository

@Transactional
@ApplicationScoped
class CreateUsersSpi : CreateUsersOut {
    companion object {
        const val MAIL_KEY = "uq_user_mail"
        const val PHONE_KEY = "uq_user_phone"
        const val REFERENCE_KEY = "uq_user_reference"
    }

    @Inject
    private lateinit var clientsRepository: ClientsRepository

    @Inject
    private lateinit var adminsRepository: AdminsRepository

    @Inject
    private lateinit var usersEntityMapper: UsersEntityMapper

    override fun addClient(user: CreateUserCommand) {
        val userEntity = usersEntityMapper.fromCreateUserToClient(user)
        try {
            Log.debug(userEntity.toString())
            clientsRepository.persist(userEntity)
            clientsRepository.flush()
        } catch (e: ConstraintViolationException) {
            handleExceptions(e)
        }
    }

    override fun addAdmin(user: CreateUserCommand) {
        val userEntity = usersEntityMapper.fromCreateUserToAdmin(user)
        try {
            Log.debug(userEntity.toString())
            adminsRepository.persist(userEntity)
            adminsRepository.flush()
        } catch (e: ConstraintViolationException) {
            handleExceptions(e)
        }
    }

    private fun handleExceptions(e:ConstraintViolationException):Throws {
        Log.debug(String.format("Error while adding admin : %s", e.message))
        Log.debug(String.format("Error while adding admin : %s", e.constraintName))
        when {
            e.constraintName.equals(MAIL_KEY) -> {
                throw ApplicationException(ApplicationExceptionsEnum.CREATE_USER_DUPLICATE_MAIL)
            }
            e.constraintName.equals(PHONE_KEY) -> {
                throw ApplicationException(ApplicationExceptionsEnum.CREATE_USER_DUPLICATE_PHONE_NUMBER)
            }
            e.constraintName.equals(REFERENCE_KEY) -> {
                throw ApplicationException(ApplicationExceptionsEnum.CREATE_USER_DUPLICATE_REFERENCE)
            }
            else -> {
                throw ApplicationException(ApplicationExceptionsEnum.ERROR)
            }
        }
    }
}