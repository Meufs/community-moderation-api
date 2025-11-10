package snb.projects.domain.utils
import io.quarkus.logging.Log;
import java.util.*

object UUIDGenerator {

    private fun generateUUID(): UUID {
        Log.info("Generating UUID reference.")
        val newRandomBusinessReference = UUID.randomUUID()
        Log.info("UUID base Reference $newRandomBusinessReference Successfully generated.")
        return newRandomBusinessReference
    }
    fun getNewUUID(): String {
        return UUIDFormatter.formatUUIDSequence(generateUUID(), "")
    }
}