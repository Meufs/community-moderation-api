package snb.projects.persistence.services.users

import com.templates.domain.errors.ApplicationException
import snb.projects.domain.errors.ApplicationExceptionsEnum
import snb.projects.persistence.repositories.ClientsRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import io.quarkus.logging.Log;
import snb.projects.domain.ports.out.UpdateMeufsOut
import java.sql.SQLException
import java.sql.Timestamp

@ApplicationScoped
@Transactional
class UpdateMeufsSpi: UpdateMeufsOut {

    @Inject
    private lateinit var clientsRepository: ClientsRepository

    override fun updateProfilePicture(mail: String, profilePictureUrl:String) {
        try {
            clientsRepository.update("profilePicture = :profilePictureUrl WHERE mail = :mail",
                mapOf(
                "mail" to mail,
                "profilePictureUrl" to profilePictureUrl
            ))
            Log.info(String.format("User %s profile picture was updated", mail))
        } catch (e: SQLException) {
            handleExceptions(e)
        }

    }

    override fun approveAccount(mail: String) {
        try {
            clientsRepository.update("accountVerifiedStatus = true WHERE mail =:mail",  mapOf(
                "mail" to mail
            ))
            Log.info(String.format("User %s was approved", mail))
        } catch (e: SQLException) {
            handleExceptions(e)
        }
    }

    override fun changeOtpCode(mail: String, newOtp: String, newOtpTimestamp: Timestamp) {
        try {
            clientsRepository.update("verificationCode = :newOtp, verificationCodeTimestamp = :newTimestamp WHERE " +
                    "mail" +
                    " =:mail",
                mapOf(
                    "newOtp" to newOtp,
                    "newTimestamp" to newOtpTimestamp,
                    "mail" to mail
                ))
            Log.info(String.format("User %s verification code updated", mail))
        } catch (e: SQLException) {
            handleExceptions(e)
        }

    }

    override fun initPasswordRecovery(
        identifier: String,
        passwordVerificationCode: String,
        passwordVerificationTimestamp: Timestamp
    ) {
        try {
            clientsRepository.update("passwordVerificationCode = :verificationCode, passwordVerificationTimestamp" +
                    " = " +
                    ":newTimestamp " +
                    "WHERE " +
                    "mail" +
                    " =:mail",
                mapOf(
                    "verificationCode" to passwordVerificationCode,
                    "newTimestamp" to passwordVerificationTimestamp,
                    "mail" to identifier
                ))
            Log.info(String.format("User %s identifier updated", identifier))
        } catch (e: SQLException) {
            handleExceptions(e)
        }
    }

    override fun changePassword(identifier: String, newPassword: String) {
        try {
            clientsRepository.update("password = :newPassword WHERE mail =:mail OR phoneNumber = :mail",
                mapOf(
                    "newPassword" to newPassword,
                    "mail" to identifier
                ))
            Log.info(String.format("User %s password updated", identifier))
        } catch (e: SQLException) {
            handleExceptions(e)
        }
    }

    private fun handleExceptions(e: SQLException) {
        Log.info(e.message)
        throw ApplicationException(ApplicationExceptionsEnum.ERROR)
    }

}