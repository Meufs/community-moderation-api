package snb.projects.domain.models.users

data class UserBasicInformations(val type:String, val reference: String, val jwToken:String, val
accountVerifiedStatus:Boolean){
    override fun toString(): String {
        return "UserBasicInformations(type='$type', reference='$reference', jwToken='$jwToken', accountVerifiedStatus=$accountVerifiedStatus)"
    }
}
