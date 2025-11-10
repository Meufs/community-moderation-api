package snb.projects.bootstrap.persistence

import com.azure.identity.ClientSecretCredentialBuilder
import com.azure.security.keyvault.secrets.SecretClientBuilder
import io.agroal.api.AgroalDataSource
import io.agroal.api.configuration.supplier.AgroalDataSourceConfigurationSupplier
import io.agroal.api.security.NamePrincipal
import io.agroal.api.security.SimplePassword
import io.agroal.api.transaction.TransactionIntegration
import io.agroal.narayana.NarayanaTransactionIntegration
import io.quarkus.hibernate.orm.PersistenceUnitExtension
import io.quarkus.hibernate.orm.runtime.customized.QuarkusConnectionProvider
import io.quarkus.hibernate.orm.runtime.tenant.TenantConnectionResolver
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.TransactionManager
import jakarta.transaction.TransactionSynchronizationRegistry
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider
import io.quarkus.logging.Log;
import java.sql.SQLException
import java.time.Duration
import java.time.temporal.ChronoUnit

@ApplicationScoped
@PersistenceUnitExtension
class DatasourceConfigurator(
    var transactionManager: TransactionManager,
    var transactionSynchronizationRegistry: TransactionSynchronizationRegistry
) :
    TenantConnectionResolver {
    @field:ConfigProperty(name = "azure.tenant-id")
    private lateinit var tenantId: String

    @field:ConfigProperty(name = "azure.tenant.token")
    private lateinit var secret: String

    @field:ConfigProperty(name = "azure.client-id")
    private lateinit var clientId: String

    @field:ConfigProperty(name = "azure.vault.url")
    private lateinit  var keyVaultUrl: String

    private fun createDatasource(): AgroalDataSource {
        val secretClient = SecretClientBuilder()
            .vaultUrl(keyVaultUrl)
            .credential(
                ClientSecretCredentialBuilder()
                    .tenantId(tenantId)
                    .clientSecret(secret)
                    .clientId(clientId)
                    .build()
            )
            .buildClient()
        Log.debug("Retrieving secrets from Azure key vault")
        val url =
            secretClient.getSecret("DB-ENDPOINT").value + "/" + secretClient.getSecret(
                "DB-NAME"
            ).value
        Log.debug("Secrets successfully retrieved")
        val dataSourceConfiguration = AgroalDataSourceConfigurationSupplier()

        val poolConfiguration = dataSourceConfiguration.connectionPoolConfiguration()

        val txIntegration: TransactionIntegration = NarayanaTransactionIntegration(
            transactionManager,
            transactionSynchronizationRegistry, null, false, null
        )
        poolConfiguration
            .initialSize(2)
            .maxSize(10)
            .minSize(2)
            .maxLifetime(Duration.of(5, ChronoUnit.MINUTES))
            .acquisitionTimeout(Duration.of(30, ChronoUnit.SECONDS))
            .transactionIntegration(txIntegration) //This part, specify transaction integration

        val connectionFactoryConfiguration = poolConfiguration.connectionFactoryConfiguration()
        Log.debug("hihihihi")
        connectionFactoryConfiguration
            .jdbcUrl(String.format("jdbc:postgresql://%s", url))
            .credential(NamePrincipal((secretClient.getSecret("DB-USER").value)))
            .credential(SimplePassword((secretClient.getSecret("DB-PASSWORD").value)))
        try {
            Log.debug("Building Datasource from secrets")
            return AgroalDataSource.from(dataSourceConfiguration.get())
        } catch (ex: SQLException) {
            throw IllegalStateException(
                "Failed to create a new data source based on the existing datasource configuration",
                ex
            )
        }
    }

    override fun resolve(tenantId: String): ConnectionProvider {
        return QuarkusConnectionProvider(createDatasource())
    }
}