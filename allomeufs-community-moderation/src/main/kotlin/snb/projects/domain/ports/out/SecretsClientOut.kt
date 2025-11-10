package snb.projects.domain.ports.out

interface SecretsClientOut {
    fun updateAdminCode(adminCode: String)
    fun getCurrentAdminCreationCode():String
}