package snb.projects.domain.ports.`in`

import org.jboss.resteasy.reactive.multipart.FileUpload


interface AzureStorageIn {
    fun updateProfilePicture(mail: String, role:String, phoneNumber:String, file: FileUpload):String
    fun createContainerForUser(phoneNumber:String)
}