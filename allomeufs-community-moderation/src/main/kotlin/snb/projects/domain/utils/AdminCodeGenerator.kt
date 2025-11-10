package snb.projects.domain.utils

import kotlin.random.Random

object AdminCodeGenerator {
    fun generateAdminCode(): String {
        val numbers = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!?()$-_&=+*"
        val until = 15
        var code = ""
        val random = Random
        for (i: Int in 0..until) {
            val number = numbers[random.nextInt(
                numbers.length
            )].toString()
            code += number
        }
        return code
    }
}