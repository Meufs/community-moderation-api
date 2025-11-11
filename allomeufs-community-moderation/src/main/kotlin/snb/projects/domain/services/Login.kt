package snb.projects.domain.services

import com.templates.domain.errors.ApplicationException
import snb.projects.domain.errors.ApplicationExceptionsEnum
import snb.projects.domain.mappers.UsersMappers
import snb.projects.domain.models.users.UserCreated
import snb.projects.domain.ports.`in`.LoginIn
import snb.projects.domain.ports.out.FindUserOut
import snb.projects.domain.services.PasswordUtils.verifyPassword
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import io.quarkus.logging.Log;

@ApplicationScoped
class Login(@field:Inject var jwtTokenGenerator: JwtTokenGenerator) : LoginIn {

    @Inject
    private lateinit var findUserOut: FindUserOut


    @Inject
    private lateinit var usersMappers: UsersMappers


    override fun login(identifier: String, password: String): UserCreated {
        val user = findUserOut.findByIdentifier(identifier)
        Log.info(user.toString())
        if(verifyPassword(password, user.password)) {
            Log.info("Login successful")
            val jwToken = jwtTokenGenerator.getToken(user.mail, user.type)
            return usersMappers.fromUsersToUsersLoggedIn(user, jwToken)
        } else {
            throw ApplicationException(ApplicationExceptionsEnum.ERROR_VALIDATING_PASSWORD_HASH)
        }
    }
}