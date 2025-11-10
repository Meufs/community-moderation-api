package snb.projects.domain.services

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import snb.projects.domain.ports.`in`.CsrfTokenGeneratorIn
import snb.projects.domain.utils.AdminCodeGenerator.generateAdminCode

@ApplicationScoped
class CsrfTokenGenerator: CsrfTokenGeneratorIn {
    @Inject
    private lateinit var csrfTokenCache: CsrfTokenCache

    override fun generateToken(identifier:String): String {
        val token = generateAdminCode()
        csrfTokenCache.addItem(identifier, token)
        return token
    }
}