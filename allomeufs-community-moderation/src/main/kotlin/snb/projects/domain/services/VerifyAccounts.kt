package snb.projects.domain.services

import com.templates.domain.errors.ApplicationException
import com.templates.domain.errors.ApplicationExceptionsEnum
import snb.projects.domain.ports.`in`.VerifyAccountsIn
import snb.projects.domain.ports.out.FindClientsOut
import snb.projects.domain.ports.out.UpdateClientsOut
import snb.projects.domain.services.PasswordUtils.hashWithBCrypt
import snb.projects.domain.services.PasswordUtils.verifyPassword
import com.templates.domain.utils.InputsValidator.hasTimestampExceededTwentyMinutes
import snb.projects.domain.utils.OtpGenerator.generateCode
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import java.sql.Timestamp
import java.time.Instant


@ApplicationScoped
class VerifyAccounts: VerifyAccountsIn {
    @Inject
    private lateinit var findClientsOut: FindClientsOut

    @Inject
    private lateinit var updateClientsOut: UpdateClientsOut

    @Inject
    private lateinit var mailer: Mailer


    override fun verifyClientAccount(mail: String, otp: String) {
        val user = findClientsOut.findByIdentifier(mail)
        val otpTimestamp = user.verificationCodeTimestamp!!
        if(verifyPassword(otp, user.verificationCode!!)){
            hasTimestampExceededTwentyMinutes(otpTimestamp, Timestamp.from(Instant.now()))
            updateClientsOut.approveAccount(mail)
        } else {
            throw  ApplicationException(ApplicationExceptionsEnum.OTP_CODES_NO_MATCH)
        }
    }

    override fun generateNewOtpCode(mail: String) {
        val newCode = generateCode()
        val hashedOtp = hashWithBCrypt(newCode).result
        val newTimestamp = Timestamp.from(Instant.now())
        val content = mailer.newOtpEmail(newCode)
        mailer.sendHtmlEmail(mail, "Mise à jour du code OTP", content)
        updateClientsOut.changeOtpCode(mail, hashedOtp, newTimestamp)
    }

}