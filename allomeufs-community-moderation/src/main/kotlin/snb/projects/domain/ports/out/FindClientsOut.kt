package snb.projects.domain.ports.out

import snb.projects.domain.models.users.User

interface FindClientsOut {
    fun findByIdentifier(identifier: String) : User
    fun findByPasswordVerificationCode(passwordVerificationCode: String) : User
}