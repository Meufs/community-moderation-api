package com.templates.domain.errors

import snb.projects.domain.errors.ApplicationExceptionsEnum


class ApplicationException(exceptionEnum: ApplicationExceptionsEnum) : RuntimeException() {
    val statusCode: Int = exceptionEnum.errorCode
    val origin: String = exceptionEnum.origin
    override val message: String = exceptionEnum.message
    constructor() : this(ApplicationExceptionsEnum.ERROR)
}