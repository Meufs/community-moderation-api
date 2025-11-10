package com.templates.domain.errors


class ApplicationException(exceptionEnum: ApplicationExceptionsEnum) : RuntimeException() {
    val statusCode: Int = exceptionEnum.errorCode
    val origin: String = exceptionEnum.origin
    override val message: String = exceptionEnum.message
    constructor() : this(ApplicationExceptionsEnum.ERROR)
}