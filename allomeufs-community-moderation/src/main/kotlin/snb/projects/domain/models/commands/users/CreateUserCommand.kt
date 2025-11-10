package snb.projects.domain.models.commands.users

import java.sql.Timestamp

class CreateUserCommand(
    val firstName: String,
    val lastName: String,
    val mail: String,
    var password: String,
    val phoneNumber: String,
    var reference: String? = null,
    var type: String ? = null,
    var verificationCode: String ? = null,
    var verificationCodeTimestamp:  Timestamp? = null,
    var accountVerified: Boolean = false,
) {
    override fun toString(): String {
        return "CreateUserCommand(firstName='$firstName', lastName='$lastName', email='$mail', password='$password', phoneNumber='$phoneNumber', reference=$reference, type=$type)"
    }
}
