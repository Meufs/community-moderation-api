package snb.projects.bootstrap.configuration

import com.azure.identity.ClientSecretCredentialBuilder
import com.azure.security.keyvault.secrets.SecretClientBuilder
import io.smallrye.config.ConfigSourceContext
import io.smallrye.config.ConfigSourceFactory
import io.smallrye.config.PropertiesConfigSource
import org.eclipse.microprofile.config.spi.ConfigSource
import java.util.*

class AzureConfigSourceFactory : ConfigSourceFactory {
	companion object {
		private const val API_KEY: String = "api.key"
		private const val CLAIMS: String = "claims.origin"
		private const val ISSUER: String = "mp.jwt.verify.issuer"
		private const val ADMIN_CODE: String = "admin.code"

	}

	override fun getConfigSources(context: ConfigSourceContext): Iterable<ConfigSource> {
		val caregiversApiKey = context.getValue(API_KEY)
		val conf = mutableMapOf<String, String>()

		if (caregiversApiKey == null || caregiversApiKey.value == null) {
			val clientId = context.getValue("azure.client-id")?.value
			val tenantId = context.getValue("azure.tenant-id")?.value
			val secret = context.getValue("azure.tenant.token")?.value
			val vaultUrl = context.getValue("azure.vault.url")?.value

			if (clientId != null && tenantId != null && secret != null && vaultUrl != null) {
				val secretClient = SecretClientBuilder()
					.vaultUrl(vaultUrl)
					.credential(
						ClientSecretCredentialBuilder()
							.tenantId(tenantId)
							.clientSecret(secret)
							.clientId(clientId)
							.build()
					)
					.buildClient()

				conf[API_KEY] = secretClient.getSecret("API-KEY").value
				conf[CLAIMS] = secretClient.getSecret("CLAIMS-ORIGIN").value
				conf[ISSUER] = secretClient.getSecret("TOKEN-ISSUER").value
				conf[ADMIN_CODE] = secretClient.getSecret("ADMIN-REGISTRATION-CODE").value
			}

			return listOf(PropertiesConfigSource(conf, null, priority))
		}

		conf[API_KEY] = caregiversApiKey.value
		return listOf(PropertiesConfigSource(conf, caregiversApiKey.sourceName, priority))
	}

	override fun getPriority(): OptionalInt {
		return OptionalInt.of(275)
	}

	private val priority: Int
		get() = getPriority().asInt
}