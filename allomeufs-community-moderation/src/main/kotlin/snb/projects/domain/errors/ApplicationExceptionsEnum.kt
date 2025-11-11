package snb.projects.domain.errors

import org.jboss.resteasy.reactive.RestResponse.StatusCode

enum class ApplicationExceptionsEnum(val message: String,val errorCode: Int,val origin: String) {
    ERROR("An error occurred", StatusCode.BAD_REQUEST, ErrorOriginEnum.BOOTSTRAP.name),
    INVALID_TOKEN("Invalid token", StatusCode.BAD_REQUEST, ErrorOriginEnum.DOMAIN.name),
    PASSWORDS_NO_MATCH("Les mots de passe ne correspondent pas", StatusCode.BAD_REQUEST, ErrorOriginEnum.DOMAIN
        .name),

    ADMIN_VERIFICATION_CODE_NO_MATCH("Le code spécial admin n'est pas valide", StatusCode.BAD_REQUEST, ErrorOriginEnum
        .DOMAIN.name),
    EMAIL_DELIVERY_FAILED("An error occurred while sending the email", StatusCode.BAD_REQUEST, ErrorOriginEnum.DOMAIN
        .name),

    ERROR_VALIDATING_PASSWORD_HASH("An error occurred while connecting your account", StatusCode.BAD_REQUEST, ErrorOriginEnum.DOMAIN
        .name),
    CREATE_USER_INVALID_PHONE_NUMBER("Invalid phone number, must be a french mobile phone number", StatusCode.BAD_REQUEST,
        ErrorOriginEnum
        .DOMAIN.name),
    CREATE_USER_INVALID_NAME("First or last name is too short", StatusCode.BAD_REQUEST, ErrorOriginEnum.DOMAIN.name),
    USER_INVALID_PASSWORD("Invalid password, password must be at least 8 characters and contain one digit, one" +
            " " +
            "lower case letter, one upper case letter, and a special character (! or ?)", StatusCode.BAD_REQUEST, ErrorOriginEnum.DOMAIN
                .name),
    CREATE_USER_DUPLICATE_MAIL("Mail address is already registered", StatusCode.BAD_REQUEST, ErrorOriginEnum.PERSISTENCE
        .name),
    CREATE_USER_DUPLICATE_PHONE_NUMBER("Phone number is already linked to an account", StatusCode.BAD_REQUEST,
        ErrorOriginEnum.PERSISTENCE
        .name),
    CREATE_USER_DUPLICATE_REFERENCE("Mail address is already registered", StatusCode.BAD_REQUEST, ErrorOriginEnum.PERSISTENCE
        .name),
    LOGIN_USER_NOT_FOUND("User not found", StatusCode.NOT_FOUND, ErrorOriginEnum.PERSISTENCE.name),
    OTP_TIMESTAMP_EXCEEDED("Le code OTP n'est plus valide, veuillez en générer un nouveau", StatusCode.BAD_REQUEST,
        ErrorOriginEnum.DOMAIN.name),
    OTP_CODES_NO_MATCH("Échec de la vérification du compte, vérifiez votre code", StatusCode.BAD_REQUEST,
        ErrorOriginEnum.DOMAIN.name),
    ;
}