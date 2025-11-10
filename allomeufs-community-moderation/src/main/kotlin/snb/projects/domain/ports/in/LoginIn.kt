package snb.projects.domain.ports.`in`

import snb.projects.domain.models.users.UserCreated

fun interface LoginIn {
    fun login(identifier: String, password: String): UserCreated
}