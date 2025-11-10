package snb.projects.domain.ports.out

import snb.projects.domain.models.users.User

fun interface FindUserOut {
    fun findByIdentifier(identifier: String) : User
}