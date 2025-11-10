package snb.projects.domain.services

import com.password4j.Hash
import com.password4j.Password

object PasswordUtils {
    fun hashWithBCrypt(password: String): Hash {
        return Password.hash(password).withBcrypt()
    }
    fun verifyPassword(password: String, hash: String): Boolean {
        return Password.check(password, hash).withBcrypt()
    }
}