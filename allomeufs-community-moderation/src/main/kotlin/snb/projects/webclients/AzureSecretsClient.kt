package snb.projects.webclients

import com.azure.identity.ClientSecretCredentialBuilder
import com.azure.security.keyvault.secrets.SecretClient
import com.azure.security.keyvault.secrets.SecretClientBuilder
import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.config.inject.ConfigProperty
import snb.projects.domain.ports.out.SecretsClientOut

@ApplicationScoped
class AzureSecretsClient: SecretsClientOut {
    var secretClient:SecretClient? = null
    @field:ConfigProperty(name = "azure.tenant-id")
    private lateinit var tenantId: String

    @field:ConfigProperty(name = "azure.tenant.token")
    private lateinit var secret: String

    @field:ConfigProperty(name = "azure.client-id")
    private lateinit var clientId: String

    @field:ConfigProperty(name = "azure.vault.url")
    private lateinit  var keyVaultUrl: String

    @PostConstruct
    fun init() {
        secretClient = SecretClientBuilder()
            .vaultUrl(keyVaultUrl)
            .credential(
                ClientSecretCredentialBuilder()
                    .tenantId(tenantId)
                    .clientSecret(secret)
                    .clientId(clientId)
                    .build()
            )
            .buildClient()
    }
    override fun updateAdminCode(adminCode: String) {
       secretClient?.setSecret("ADMIN-REGISTRATION-CODE", adminCode)
    }

    override fun getCurrentAdminCreationCode(): String {
        return secretClient?.getSecret("ADMIN-REGISTRATION-CODE")?.value!!
    }
}