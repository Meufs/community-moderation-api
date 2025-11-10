package snb.projects.domain.utils

import kotlin.random.Random

object OtpGenerator {
    fun generateCode(): String {
        val numbers = "0123456789"
        val code = StringBuilder();
        repeat(6) {
            code.append(
                numbers[Random.nextInt(
                    numbers.length
                )]
            )
        }
        return code.toString()
    }
}