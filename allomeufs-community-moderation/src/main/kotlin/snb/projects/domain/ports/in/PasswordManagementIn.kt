package snb.projects.domain.ports.`in`

interface PasswordManagementIn {
    fun initPasswordRecovery(identifier: String)
    fun recoverPassword(mail: String, token: String, password: String, passwordConfirmation: String)
    fun changePassword(mail:String, password: String, passwordConfirmation: String)
}