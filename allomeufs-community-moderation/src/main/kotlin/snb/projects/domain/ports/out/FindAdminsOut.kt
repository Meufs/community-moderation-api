package snb.projects.domain.ports.out

import snb.projects.domain.models.users.User

fun interface FindAdminsOut {
    fun findByIdentifier(identifier: String) : User
}