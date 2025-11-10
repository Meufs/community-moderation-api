package snb.projects.domain.ports.out

import java.sql.Timestamp


interface UpdateAdminsOut {
    fun updateProfilePicture(mail: String, profilePictureUrl: String)
    fun initPasswordRecovery(identifier: String, passwordVerificationCode: String, passwordVerificationTimestamp:
    Timestamp)
    fun changePassword(identifier: String, newPassword: String)
}