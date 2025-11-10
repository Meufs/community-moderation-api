package snb.projects.domain.models.users

import java.sql.Timestamp

class User(
    val id :Int,
    val firstName: String,
    val lastName: String,
    val mail: String,
    val password: String,
    val phoneNumber: String,
    val reference: String,
    val type: String,
    var verificationCode: String? = null,
    var verificationCodeTimestamp: Timestamp? = null,
    val profilePicture: String? = null,
    var accountVerifiedStatus: Boolean? = false,
    var passwordVerificationCode: String? = null,
    var passwordVerificationTimestamp: Timestamp? = null,
){
    override fun toString(): String {
        return "User(id=$id, firstName='$firstName', lastName='$lastName', mail='$mail', password='$password', phoneNumber='$phoneNumber', reference='$reference', type='$type', verificationCode=$verificationCode, verificationCodeTimestamp=$verificationCodeTimestamp, profilePicture=$profilePicture, accountVerifiedStatus=$accountVerifiedStatus, passwordVerificationCode=$passwordVerificationCode, passwordVerificationTimestamp=$passwordVerificationTimestamp)"
    }
}
