package snb.projects.domain.services

import com.azure.communication.email.EmailClientBuilder
import com.azure.communication.email.models.EmailAddress
import com.azure.communication.email.models.EmailMessage
import com.azure.communication.email.models.EmailSendResult
import com.azure.core.util.polling.PollResponse
import com.azure.core.util.polling.SyncPoller
import com.templates.domain.errors.ApplicationException
import com.templates.domain.errors.ApplicationExceptionsEnum
import io.quarkus.qute.Location
import io.quarkus.qute.Template
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.config.inject.ConfigProperty


@ApplicationScoped
class Mailer {
    @ConfigProperty(name = "azure.comm-service.endpoint")
    private lateinit var azureCommunicationEndpoint: String

    @ConfigProperty(name = "azure.comm-service.access.key")
    private lateinit var azureCommunicationAccessKey: String

    @ConfigProperty(name = "azure.comm-service.mailer.sender.do.not.reply")
    private lateinit var azureDoNotReplySender: String

    @Location("otp-email.html")
    private lateinit var otpMail: Template

    @Location("new-otp-mail.html")
    private lateinit var newOtpMail: Template

    @Location("password-recovery-email.html")
    private lateinit var passwordRecoveryMail: Template

    fun sendHtmlEmail(recipient: String, subject:String, content: String) {
        val builder =  StringBuilder()
        builder.append(azureCommunicationEndpoint)
        builder.append(azureCommunicationAccessKey)
        val emailClient = EmailClientBuilder().connectionString(builder.toString()).buildClient()
        val toAddress = EmailAddress(recipient)

        val emailMessage: EmailMessage = EmailMessage()
            .setSenderAddress(azureDoNotReplySender)
            .setToRecipients(toAddress)
            .setSubject(subject)
            .setBodyHtml(
                content.trimIndent()
            )

        val poller: SyncPoller<EmailSendResult, EmailSendResult> = emailClient.beginSend(emailMessage, null)
        val result: PollResponse<EmailSendResult> = poller.waitForCompletion()
        if(result.value.status.value != "Succeeded"){
            throw ApplicationException(ApplicationExceptionsEnum.EMAIL_DELIVERY_FAILED)
        }
    }

    fun generateOtpEmail(userName: String, verificationCode: String): String {
        return otpMail
            .data("userName", userName)
            .data("verificationCode", verificationCode)
            .render()
    }
    fun generatePasswordRecoveryEmail(token:String): String {
        val url = String.format("http://localhost:5173/reset-password/%s",token)
        return passwordRecoveryMail
            .data("url", url)
            .render()
    }

    fun newOtpEmail(verificationCode: String): String {
        return newOtpMail
            .data("verificationCode", verificationCode)
            .render()
    }
}