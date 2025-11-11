package com.templates.domain.utils

import com.templates.domain.errors.ApplicationException
import snb.projects.domain.errors.ApplicationExceptionsEnum
import snb.projects.domain.services.PasswordUtils.verifyPassword
import java.sql.Timestamp
import kotlin.math.abs

object InputsValidator {
    fun validatePasswordConfirmation(inputPassword: String, confirmPassword: String) {
        if(inputPassword != confirmPassword){
            throw ApplicationException(ApplicationExceptionsEnum.PASSWORDS_NO_MATCH)
        }
    }
    fun validatePasswordFormat(inputPassword: String) {
        if(!inputPassword.matches(Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!?\\\$_])[A-Za-z\\d!?\\\$_]{8,}\$"))){
            throw ApplicationException(ApplicationExceptionsEnum.USER_INVALID_PASSWORD)
        }
    }

    fun validatePhoneNumberFormat(phoneNumber: String) {
        if(!phoneNumber.matches(Regex("^0[67]\\d{8}$"))){
            throw ApplicationException(ApplicationExceptionsEnum.CREATE_USER_INVALID_PHONE_NUMBER)
        }
    }

    fun validatePasswordHash(token:String, hashedToken:String) {
        if(!verifyPassword(token, hashedToken)){
            throw ApplicationException(ApplicationExceptionsEnum.INVALID_TOKEN)
        }
    }

    fun hasTimestampExceededTwentyMinutes(timestamp1: Timestamp, timestamp2: Timestamp) {
        val millisecondsDifference = abs(timestamp2.time - timestamp1.time)
        if( millisecondsDifference > 1200000){
            throw ApplicationException(ApplicationExceptionsEnum.OTP_TIMESTAMP_EXCEEDED)

        }
    }
}