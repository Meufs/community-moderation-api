package snb.projects.domain.ports.`in`

fun interface CsrfTokenGeneratorIn {
    fun generateToken(identifier:String): String
}