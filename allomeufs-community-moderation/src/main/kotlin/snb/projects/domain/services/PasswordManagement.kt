package snb.projects.domain.services

import snb.projects.domain.ports.`in`.PasswordManagementIn
import snb.projects.domain.ports.out.FindClientsOut
import snb.projects.domain.ports.out.UpdateClientsOut
import snb.projects.domain.services.PasswordUtils.hashWithBCrypt
import snb.projects.domain.utils.AdminCodeGenerator.generateAdminCode
import com.templates.domain.utils.InputsValidator.hasTimestampExceededTwentyMinutes
import com.templates.domain.utils.InputsValidator.validatePasswordConfirmation
import com.templates.domain.utils.InputsValidator.validatePasswordFormat
import com.templates.domain.utils.InputsValidator.validatePasswordHash
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import io.quarkus.logging.Log;
import java.sql.Timestamp
import java.time.Instant

@ApplicationScoped
class PasswordManagement : PasswordManagementIn {

    @Inject
    private lateinit var mailer: Mailer

    @Inject
    private lateinit var updateClientsOut: UpdateClientsOut

    @Inject
    private lateinit var findClientsOut: FindClientsOut

    override fun initPasswordRecovery(identifier: String) {
        val user = findClientsOut.findByIdentifier(identifier)
        val mail = user.mail
        val token = generateAdminCode()
        Log.info(token)
        val mailContent = mailer.generatePasswordRecoveryEmail(token)
        val safeToken = hashWithBCrypt(token).result
        val tokenTimestamp = Timestamp.from(Instant.now())
        mailer.sendHtmlEmail(mail, "Récupération de mot de passe", mailContent)
        updateClientsOut.initPasswordRecovery(mail, safeToken, tokenTimestamp)
    }

    override fun recoverPassword( mail:String,token: String, password: String, passwordConfirmation: String) {
        val user = findClientsOut.findByIdentifier(mail)
        val hashedToken = user.passwordVerificationCode!!
        val currentTimestamp = user.passwordVerificationTimestamp!!
        validatePasswordFormat(password)
        validatePasswordConfirmation(password, passwordConfirmation)
        validatePasswordHash(token, hashedToken)
        hasTimestampExceededTwentyMinutes(currentTimestamp, Timestamp.from(Instant.now()))
        val hashedPw = hashWithBCrypt(password).result
        updateClientsOut.changePassword(user.mail, hashedPw)
    }


    override fun changePassword(mail: String, password: String, passwordConfirmation: String) {
        validatePasswordFormat(password)
        validatePasswordConfirmation(password, passwordConfirmation)
        val hashedPw = hashWithBCrypt(password).result
        updateClientsOut.changePassword(mail, hashedPw)
    }


}