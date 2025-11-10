package snb.projects.domain.services

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import snb.projects.domain.ports.`in`.AdminCodeIn
import snb.projects.domain.ports.out.SecretsClientOut

@ApplicationScoped
class AdminCode : AdminCodeIn {
    @Inject
    private lateinit var secretsClientOut: SecretsClientOut

    override fun getCurrentCode(): String {
        return secretsClientOut.getCurrentAdminCreationCode()
    }
}