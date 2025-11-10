package snb.projects.domain.models.users

class UserCreated(
    val firstName: String,
    val lastName: String,
    val mail: String,
    val phoneNumber: String,
    val reference: String,
    val type: String,
    val jwToken:String,
    val profilePicture:String?=null,
    val accountVerifiedStatus:Boolean
){
    override fun toString(): String {
        return "UserCreated(firstName='$firstName', lastName='$lastName', mail='$mail', phoneNumber='$phoneNumber', " +
                "reference='$reference', type='$type', jwToken='$jwToken', profilePicture='$profilePicture', accountVerifiedStatus=$accountVerifiedStatus)"
    }
}
