package snb.projects.domain.services

import com.templates.domain.errors.ApplicationException
import com.templates.domain.errors.ApplicationExceptionsEnum
import snb.projects.domain.models.commands.users.CreateUserCommand
import snb.projects.domain.models.users.UserBasicInformations
import snb.projects.domain.models.users.UserTypes
import snb.projects.domain.ports.`in`.AzureStorageIn
import snb.projects.domain.ports.`in`.CreateUsersIn
import com.templates.domain.ports.out.CreateUsersOut
import snb.projects.domain.ports.out.SecretsClientOut
import snb.projects.domain.services.PasswordUtils.hashWithBCrypt
import snb.projects.domain.utils.AdminCodeGenerator.generateAdminCode
import com.templates.domain.utils.InputsValidator.validatePasswordFormat
import com.templates.domain.utils.InputsValidator.validatePhoneNumberFormat
import snb.projects.domain.utils.OtpGenerator
import snb.projects.domain.utils.UUIDGenerator.getNewUUID
import io.quarkus.logging.Log
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.sql.Timestamp

@ApplicationScoped
class CreateUsers : CreateUsersIn {

    @Inject
    private lateinit var jwtTokenGenerator: JwtTokenGenerator
    @Inject
    private lateinit var mailer: Mailer
    @Inject
    private lateinit var createUsersOut: CreateUsersOut
    @Inject
    private lateinit var azureStorageIn: AzureStorageIn
    @Inject
    private lateinit var secretsClientOut: SecretsClientOut
    @field:ConfigProperty(name = "admin.code")
    private lateinit var adminCreationCode: String

    override fun createUser(user: CreateUserCommand): UserBasicInformations {
        Log.info("Creating user")
        val userType = UserTypes.CLIENT.name
        val userReference = setUpUserDataAndCheckInputs(user, userType)
        val userToken = jwtTokenGenerator.getToken(user.mail,userType)
        createUsersOut.addClient(user)
        azureStorageIn.createContainerForUser(user.phoneNumber)
        Log.info("OTP verification Mail sent to user")
       return UserBasicInformations(userType, userReference, userToken, false)
    }



    override fun createAdmin(user: CreateUserCommand, adminCode: String): UserBasicInformations {
        Log.debug(adminCreationCode)
        Log.debug(adminCode)
        if(adminCode == adminCreationCode){
            val userType = UserTypes.ADMIN.name
            val userReference = setUpUserDataAndCheckInputs(user, userType)
            val userToken = jwtTokenGenerator.getToken(user.mail,userType)
            user.accountVerified = true
            createUsersOut.addAdmin(user)
            azureStorageIn.createContainerForUser(user.phoneNumber)
            Log.info("OTP verification Mail sent to user")
            val newAdminCreationCode = generateAdminCode()
            adminCreationCode = newAdminCreationCode
            secretsClientOut.updateAdminCode(adminCreationCode)
            Log.debug(adminCreationCode)
            return UserBasicInformations(userType, userReference, userToken, false)
        } else {
            throw ApplicationException(ApplicationExceptionsEnum.ADMIN_VERIFICATION_CODE_NO_MATCH)
        }
    }

    fun setUpUserDataAndCheckInputs(
        user: CreateUserCommand,
        userType: String
    ): String {
        val userReference = getNewUUID()
        val preHashPW = user.password
        val verificationCode = OtpGenerator.generateCode()
        val content = mailer.generateOtpEmail(user.firstName, verificationCode)

        user.type = userType
        user.reference = userReference
        if(user.type == UserTypes.CLIENT.name){
            mailer.sendHtmlEmail(user.mail, "Vérification de compte", content)
        }

        verifyCreateUserInputs(preHashPW, user)
        val hash = hashWithBCrypt(preHashPW)
        user.verificationCode = hashWithBCrypt( verificationCode).result
        user.verificationCodeTimestamp = Timestamp(System.currentTimeMillis())
        user.password = hash.result
        return userReference
    }

    fun verifyCreateUserInputs(preHashPW: String, user: CreateUserCommand){
        validatePasswordFormat(preHashPW)
        if(user.lastName.length < 3 || user.firstName.length < 2 ) {
            throw ApplicationException(ApplicationExceptionsEnum.CREATE_USER_INVALID_NAME)
        }
       validatePhoneNumberFormat(user.phoneNumber)
    }
}