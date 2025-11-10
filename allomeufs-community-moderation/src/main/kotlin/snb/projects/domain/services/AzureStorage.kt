package snb.projects.domain.services

import com.azure.identity.ClientSecretCredentialBuilder
import com.azure.storage.blob.BlobClient
import com.azure.storage.blob.BlobContainerClient
import com.azure.storage.blob.BlobServiceClient
import com.azure.storage.blob.BlobServiceClientBuilder
import com.azure.storage.blob.models.PublicAccessType
import com.templates.domain.errors.ApplicationException
import com.templates.domain.errors.ApplicationExceptionsEnum
import snb.projects.domain.ports.`in`.AzureStorageIn
import snb.projects.domain.ports.out.UpdateAdminsOut
import snb.projects.domain.ports.out.UpdateMeufsOut
import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.jboss.resteasy.reactive.multipart.FileUpload
import io.quarkus.logging.Log;


@ApplicationScoped
class AzureStorage : AzureStorageIn {
    companion object {
        const val FORMATTER: String = "user-%s"
    }


    @field:ConfigProperty(name = "azure.client-id")
    private lateinit var clientId: String

    @field:ConfigProperty(name = "azure.tenant-id")
    private lateinit var tenantId: String

    @field:ConfigProperty(name = "azure.tenant.token")
    private lateinit var clientSecret: String

    @field:ConfigProperty(name = "azure.store.endpoint")
    private lateinit var endpoint: String

    @Inject
    private lateinit var updateMeufsOut: UpdateMeufsOut

    @Inject
    private lateinit var updateAdminsOut: UpdateAdminsOut

    var blobServiceClient: BlobServiceClient? = null

    @PostConstruct
    fun init() {
        blobServiceClient = BlobServiceClientBuilder()
            .endpoint(endpoint)
            .credential(
                ClientSecretCredentialBuilder()
                    .tenantId(tenantId)
                    .clientId(clientId)
                    .clientSecret(clientSecret).build()
            )
            .buildClient()
    }

    override fun updateProfilePicture(mail: String, role:String, phoneNumber:String, file: FileUpload): String {
        val containerName = String.format(FORMATTER, phoneNumber)
        val fileName = "profile-picture${file.name().substringAfterLast(".")}"
        try {
            val containerClient: BlobContainerClient = blobServiceClient!!.getBlobContainerClient(containerName)
            containerClient.listBlobs().forEach{ blob ->
                containerClient.getBlobClient(blob.name).delete()
            }
            val client: BlobClient = containerClient.getBlobClient(fileName)
            client.uploadFromFile(
                file.filePath().toString()
            )
            val profilePictureUrl = client.blobUrl
            if(role == "MEUF" || role == "MODO"){
                updateMeufsOut.updateProfilePicture(mail, profilePictureUrl)
            } else {
                updateAdminsOut.updateProfilePicture(mail, profilePictureUrl)
            }
            Log.info("Profile picture updated : $profilePictureUrl")
            return profilePictureUrl
        } catch (e: Exception) {
            Log.debug(e.toString())
            throw ApplicationException(ApplicationExceptionsEnum.ERROR)
        }
    }

    override fun createContainerForUser(phoneNumber: String) {
        val containerName = String.format(FORMATTER, phoneNumber)
        try {
            blobServiceClient!!.createBlobContainer(containerName)
            val blobServiceCLient = blobServiceClient!!.getBlobContainerClient(containerName)
            blobServiceCLient.setAccessPolicy(PublicAccessType.BLOB, null)
            Log.debug("Container created for user with phone number $phoneNumber")
        } catch (e: Exception) {
            Log.debug(e.message)
            throw ApplicationException(ApplicationExceptionsEnum.ERROR)
        }
    }
}